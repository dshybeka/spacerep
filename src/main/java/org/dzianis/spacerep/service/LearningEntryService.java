package org.dzianis.spacerep.service;

import static com.google.common.collect.ImmutableList.toImmutableList;

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
import org.springframework.beans.factory.annotation.Autowired;

public class LearningEntryService {

  private static final Logger LOG = LoggerFactory.getLogger(LearningEntryService.class);

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

  private static final double EASINESS_FACTOR_ON_CREATE = 2.5;
  private static final int MAX_ACTIVE_ENTRIES_PER_LOAD = 3;

  private final Converter<LearningEntry, LearningEntryProto> learningEntryConverter;
  private final TimeSource timeSource;
  private final SchedulingService schedulingService;
  private final LearningEntryDao learningEntryDao;
  private final LocalDateConverter localDateConverter;

  @Autowired
  public LearningEntryService(
      Converter<LearningEntry, LearningEntryProto> learningEntryConverter,
      TimeSource timeSource,
      SchedulingService schedulingService,
      LearningEntryDao learningEntryDao,
      LocalDateConverter localDateConverter) {
    this.learningEntryConverter = learningEntryConverter;
    this.timeSource = timeSource;
    this.schedulingService = schedulingService;
    this.learningEntryDao = learningEntryDao;
    this.localDateConverter = localDateConverter;
  }

  public LearningEntryProto get(long id) {
    return learningEntryDao
        .get(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Learning entry with id " + id + " not found."));
  }

  public ImmutableList<LearningEntryProto> readAllActive() {
    return learningEntryDao.getAll().stream()
        .filter(this::isActive)
        .limit(MAX_ACTIVE_ENTRIES_PER_LOAD)
        .collect(toImmutableList());
  }

  public LearningEntryProto createNew(CreateLearningEntry request) {
    Preconditions.checkNotNull(request.getName(), "Name should not be null.");

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
                Optional.ofNullable(request.getScheduleFor())
                    .orElseGet(schedulingService::scheduleOnCreate))
            .build();

    LearningEntryProto createdEntry =
        learningEntryDao.insert(learningEntryConverter.convert(learningEntry));

    LOG.info("Learning entry with id: {} created.", createdEntry.getId());

    return createdEntry;
  }

  public LearningEntryProto updateWithoutMark(UpdateLearningEntry request) {
    Preconditions.checkArgument(
        request.getMarkValue() > 0, "New mark value should be greater than 0.");
    LearningEntryProto storedEntry =
        learningEntryDao
            .get(request.getId())
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
            .scheduledFor(request.getScheduleFor())
            .updatedAt(timeSource.now())
            .attempt(request.getAttempt())
            .mark(learningEntry.getLastMark().toBuilder().setValue(request.getMarkValue()).build());

    LearningEntryProto convertedEntry = learningEntryConverter.convert(builder.build());
    learningEntryDao.update(convertedEntry);

    LOG.info("Learning entry with id: {} updated.", learningEntry.getId());

    return convertedEntry;
  }

  // TODO: add separate methods to only update data or to update mark and reschedule only.
  public LearningEntryProto updateMarkAndReschedule(UpdateLearningEntry request) {
    LearningEntryProto storedEntry =
        learningEntryDao
            .get(request.getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Learning entry with id " + request.getId() + " does not exist."));

    final LearningEntry learningEntry = learningEntryConverter.reverse().convert(storedEntry);
    EasinessFactor newEasinessFactor =
        calculateEasinessFactor(learningEntry, request.getMarkValue());

    LearningEntryBuilder builder =
        learningEntry
            .toBuilder()
            .name(request.getName())
            .notes(request.getNotes())
            .status(request.getStatus())
            .link(request.getLink())
            .scheduledFor(
                Optional.ofNullable(request.getScheduleFor())
                    .orElseGet(() -> schedulingService.schedule(learningEntry)))
            .lastMark(
                Mark.newBuilder()
                    .setValue(request.getMarkValue())
                    .setDate(timeSource.timestampNow())
                    .build())
            .lastEasinessFactor(newEasinessFactor)
            .updatedAt(timeSource.now())
            .attempt(learningEntry.getAttempt() + 1)
            .scheduledFor(schedulingService.schedule(learningEntry))
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
    LocalDate scheduledFor = localDateConverter.toLocalDateTime(entry.getScheduledFor());
    LocalDate now = timeSource.localDateNow();

    return scheduledFor.isBefore(now);
  }
}
