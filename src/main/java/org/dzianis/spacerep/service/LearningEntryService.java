package org.dzianis.spacerep.service;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.dzianis.spacerep.controller.model.CreateLearningEntryRequest;
import org.dzianis.spacerep.controller.model.UpdateLearningEntryRequest;
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

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
  private static final double EASINESS_FACTOR_ON_CREATE = 2.5;

  private final Converter<LearningEntry, LearningEntryProto> learningEntryConverter;
  private final TimeSource timeSource;
  private final SchedulingService schedulingService;
  private final LearningEntryDao learningEntryDao;

  @Autowired
  public LearningEntryService(
      Converter<LearningEntry, LearningEntryProto> learningEntryConverter,
      TimeSource timeSource,
      SchedulingService schedulingService,
      LearningEntryDao learningEntryDao) {
    this.learningEntryConverter = learningEntryConverter;
    this.timeSource = timeSource;
    this.schedulingService = schedulingService;
    this.learningEntryDao = learningEntryDao;
  }

  public LearningEntryProto get(long id) {
    return learningEntryDao
        .get(id)
        .orElseThrow(
            () -> new IllegalArgumentException("Learning entry with id " + id + " not found."));
  }

  public LearningEntryProto createNew(CreateLearningEntryRequest request) {
    Preconditions.checkNotNull(request.getName(), "Name should not be null.");

    LearningEntry learningEntry =
        LearningEntry.builder()
            .name(request.getName())
            .notes(request.getNotes())
            .attempt(request.getAttempt())
            .links(request.getLinks())
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

  public LearningEntryProto update(UpdateLearningEntryRequest request) {
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
            .links(request.getLinks())
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

    LOG.info("Learning entry with id: {} updated.", learningEntry.getId());

    return convertedEntry;
  }

  /** Calculate next easiness factor: EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02)) but no lower than 1.3 */
  private EasinessFactor calculateEasinessFactor(LearningEntry learningEntry, int markValue) {
    double ef = learningEntry.getLastEasinessFactor().getValue();
    int q = markValue;

    double newEf = ef + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02));

    return EasinessFactor.newBuilder().setValue(newEf).setDate(timeSource.timestampNow()).build();
  }
}
