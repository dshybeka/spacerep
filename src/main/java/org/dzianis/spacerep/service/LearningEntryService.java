package org.dzianis.spacerep.service;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.time.temporal.ChronoUnit.DAYS;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.dzianis.spacerep.controller.model.CreateLearningEntry;
import org.dzianis.spacerep.controller.model.UpdateLearningEntry;
import org.dzianis.spacerep.converter.LocalDateConverter;
import org.dzianis.spacerep.dao.LearningEntryDao;
import org.dzianis.spacerep.model.LearningEntry;
import org.dzianis.spacerep.model.LearningEntry.LearningEntryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Mark;
import org.spacerep.protos.Status;

public class LearningEntryService {

  private static final Logger LOG = LoggerFactory.getLogger(LearningEntryService.class);

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

  private static final double EASINESS_FACTOR_ON_CREATE = 2.5;
  private static final int MAX_ATTEMPTS_TO_REPEAT = 10;

  private final Converter<LearningEntry, LearningEntryProto> learningEntryConverter;
  private final TimeSource timeSource;
  private final SchedulingService schedulingService;
  private final LearningEntryDao learningEntryDao;
  private final LocalDateConverter localDateConverter;

  public LearningEntryService(
      Converter<LearningEntry, LearningEntryProto> learningEntryConverter,
      TimeSource timeSource,
      SchedulingService schedulingService,
      LearningEntryDao datastoreLearningEntryDao,
      LocalDateConverter localDateConverter) {
    this.learningEntryConverter = learningEntryConverter;
    this.timeSource = timeSource;
    this.schedulingService = schedulingService;
    this.learningEntryDao = datastoreLearningEntryDao;
    this.localDateConverter = localDateConverter;
  }

  public LearningEntryProto get(String uuid) {
    return learningEntryDao
        .get(uuid)
        .orElseThrow(
            () -> new IllegalArgumentException("Learning entry with uuid " + uuid + " not found."));
  }

  public ImmutableList<LearningEntryProto> readAllActive() {
    return learningEntryDao.getAll().stream().filter(this::isActive).collect(toImmutableList());
  }

  public ImmutableList<LearningEntryProto> readAll() {
    return learningEntryDao.getAll().stream().collect(toImmutableList());
  }

  public ImmutableList<LearningEntryProto> readArchive() {
    return learningEntryDao.getAll().stream()
        .filter(entry -> entry.getArchivedAt().getSeconds() > 0)
        .collect(toImmutableList());
  }

  public LearningEntryProto createNew(CreateLearningEntry request) {
    Preconditions.checkNotNull(request.getName(), "Name should not be null.");
    Preconditions.checkArgument(
        !learningEntryDao.containsName(request.getName()), request.getName() + " created already.");

    LearningEntry learningEntry =
        LearningEntry.builder()
            .name(request.getName())
            .notes(request.getNotes())
            .attempt(request.getAttempt())
            .link(request.getLink())
            .createdAt(timeSource.now())
            .updatedAt(timeSource.now())
            .status(Status.SCHEDULED)
            .lastEasinessFactor(
                EasinessFactor.newBuilder()
                    .setValue(EASINESS_FACTOR_ON_CREATE)
                    .setDate(timeSource.timestampNow())
                    .build())
            .scheduledFor(
                Optional.ofNullable(request.getScheduledFor())
                    .orElseGet(schedulingService::scheduleOnCreate))
            .delayInDays(request.getDelayInDays())
            .build();

    LearningEntryProto createdEntry =
        learningEntryDao.insert(learningEntryConverter.convert(learningEntry));

    LOG.info("Learning entry with id: {} created.", createdEntry.getId());

    return createdEntry;
  }

  public LearningEntryProto updateWithoutProcess(UpdateLearningEntry request) {
    Preconditions.checkArgument(
        request.getMarkValue() > 0, "New mark value should be greater than 0.");
    LearningEntryProto storedEntry =
        learningEntryDao
            .get(request.getUuid())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Learning entry with id " + request.getId() + " does not exist."));

    final LearningEntry learningEntry = learningEntryConverter.reverse().convert(storedEntry);
    LearningEntryBuilder builder =
        learningEntry
            .toBuilder()
            .name(request.getName())
            .notes(request.getNotes())
            .status(request.getStatus())
            .link(request.getLink())
            .scheduledFor(request.getScheduledFor())
            .updatedAt(timeSource.now())
            .attempt(request.getAttempt())
            .lastMark(
                learningEntry.getLastMark().toBuilder().setValue(request.getMarkValue()).build())
            .delayInDays(
                Optional.ofNullable(request.getDelayInDays()).orElse(storedEntry.getDelayInDays()));

    LearningEntryProto convertedEntry = learningEntryConverter.convert(builder.build());
    learningEntryDao.update(convertedEntry);

    LOG.info("Learning entry with id: {} updated.", learningEntry.getId());

    return convertedEntry;
  }

  public LearningEntryProto updateMarkAndReschedule(UpdateLearningEntry request) {
    LearningEntryProto storedEntry =
        learningEntryDao
            .get(request.getUuid())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Learning entry with id " + request.getId() + " does not exist."));

    final LearningEntry learningEntry = learningEntryConverter.reverse().convert(storedEntry);
    EasinessFactor newEasinessFactor =
        calculateEasinessFactor(learningEntry, request.getMarkValue());

    int nextAttempt = learningEntry.getAttempt() + 1;
    LocalDate nextScheduledFor =
        Optional.ofNullable(request.getScheduledFor())
            .orElseGet(() -> schedulingService.schedule(learningEntry));
    LearningEntryBuilder builder =
        learningEntry
            .toBuilder()
            .name(request.getName())
            .notes(request.getNotes())
            .status(
                nextAttempt > MAX_ATTEMPTS_TO_REPEAT ? Status.ARCHIVED : learningEntry.getStatus())
            .link(request.getLink())
            .scheduledFor(nextScheduledFor)
            .lastMark(
                Mark.newBuilder()
                    .setValue(request.getMarkValue())
                    .setDate(timeSource.timestampNow())
                    .build())
            .lastEasinessFactor(newEasinessFactor)
            .updatedAt(timeSource.now())
            .attempt(nextAttempt)
            .delayInDays(
                Math.abs(
                    Math.toIntExact(
                        DAYS.between(nextScheduledFor, learningEntry.getScheduledFor()))))
            .change(
                String.format(
                    "Updated with ef: %s and scheduled: %s",
                    newEasinessFactor.getValue(),
                    FORMATTER.format(learningEntry.getScheduledFor())));
    if (!Mark.getDefaultInstance().equals(learningEntry.getLastMark())) {
      builder.mark(learningEntry.getLastMark());
    }
    if (!EasinessFactor.getDefaultInstance().equals(learningEntry.getLastEasinessFactor())) {
      builder.easinessFactor(learningEntry.getLastEasinessFactor());
    }

    LearningEntryProto convertedEntry = learningEntryConverter.convert(builder.build());
    learningEntryDao.update(convertedEntry);

    LOG.info("Learning entry with id: {} updated and rescheduled.", learningEntry.getId());

    return convertedEntry;
  }

  /** Calculate next easiness factor: EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02)) but no lower than 1.3 */
  private EasinessFactor calculateEasinessFactor(LearningEntry learningEntry, int markValue) {
    double ef = learningEntry.getLastEasinessFactor().getValue();
    int q = markValue;

    double newEf = ef + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02));

    return EasinessFactor.newBuilder().setValue(newEf).setDate(timeSource.timestampNow()).build();
  }

  private boolean isActive(LearningEntryProto entry) {
    LocalDate scheduledFor = localDateConverter.toLocalDate(entry.getScheduledFor());
    LocalDate now = timeSource.localDateNow();

    return scheduledFor.isBefore(now) || scheduledFor.equals(now);
  }

  public void delete(String id) {
    learningEntryDao.delete(id);
  }
}
