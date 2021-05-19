package org.dzianis.spacerep.service;

import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.time.format.DateTimeFormatter;
import org.dzianis.spacerep.dao.LearningEntryDao;
import org.dzianis.spacerep.model.LearningEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
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

  public LearningEntry createNew(LearningEntryProto learningEntryProto) {
    Preconditions.checkNotNull(learningEntryProto, "New learning entry should not be null.");

    LearningEntry learningEntry = learningEntryConverter.reverse().convert(learningEntryProto);
    learningEntry =
        learningEntry
            .toBuilder()
            .createdAt(timeSource.now())
            .updatedAt(timeSource.now())
            .attempt(1)
            .status(Status.SCHEDULED)
            .easinessFactor(
                EasinessFactor.newBuilder()
                    .setValue(EASINESS_FACTOR_ON_CREATE)
                    .setDate(timeSource.timestampNow())
                    .build())
            .scheduledFor(schedulingService.schedule(learningEntry))
            .build();

    learningEntryDao.insert(learningEntryConverter.convert(learningEntry));

    LOG.info("Learning entry with id: {} created.", learningEntry.getId());

    return learningEntry;
  }

  public LearningEntry update(LearningEntryProto learningEntryProto) {
    LearningEntry learningEntry = learningEntryConverter.reverse().convert(learningEntryProto);
    learningEntry =
        learningEntry
            .toBuilder()
            .updatedAt(timeSource.now())
            .attempt(learningEntry.getAttempt() + 1)
            .easinessFactor(EasinessFactor.newBuilder().setValue(EASINESS_FACTOR_ON_CREATE).build())
            .scheduledFor(schedulingService.schedule(learningEntry))
            .easinessFactor(calculateEasinessFactor(learningEntry))
            .change(
                String.format(
                    "Updated with ef: %s and scheduled: %s",
                    learningEntry.getId(), FORMATTER.format(learningEntry.getScheduledFor())))
            .build();

    learningEntryDao.update(learningEntryConverter.convert(learningEntry));

    LOG.info("Learning entry with id: {} updated.", learningEntry.getId());

    return learningEntry;
  }

  /** Calculate next easiness factor: EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02)) but no lower than 1.3 */
  private EasinessFactor calculateEasinessFactor(LearningEntry learningEntry) {
    double ef = learningEntry.getLatestEasinessFactor().getValue();
    int q = learningEntry.getLatestMark().getValue();

    double newEf = ef + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02));

    return EasinessFactor.newBuilder().setValue(newEf).setDate(timeSource.timestampNow()).build();
  }
}
