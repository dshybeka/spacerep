package org.dzianis.spacerep.service;

import java.time.Duration;
import java.time.LocalDateTime;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.EasinessFactor;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulingService {

  private static final int SCHEDULE_DELAY_ON_CREATE = 7;
  public static final int MAX_SCHEDULE_DELAY_DAYS = 150;

  private final TimeSource timeSource;

  @Autowired
  public SchedulingService(TimeSource timeSource) {
    this.timeSource = timeSource;
  }

  LocalDateTime schedule(LearningEntry learningEntry) {
    if (learningEntry.getScheduledFor() == null) {
      return scheduleOnCreate(learningEntry);
    }

    return scheduleOnUpdate(learningEntry);
  }

  /**
   * Next date is calculated using following formula: I(n):=I(n-1)*eFactor.
   *
   * @return
   */
  private LocalDateTime scheduleOnUpdate(LearningEntry learningEntry) {
    EasinessFactor easinessFactor = learningEntry.getLatestEasinessFactor();

    LocalDateTime lastUpdated = learningEntry.getUpdatedAt();
    LocalDateTime scheduledDate = learningEntry.getUpdatedAt();

    long previousDelayDays = Duration.between(lastUpdated, scheduledDate).toDays();
    long nextDelay =
        Math.min(
            Math.round(previousDelayDays * easinessFactor.getValue()), MAX_SCHEDULE_DELAY_DAYS);

    return timeSource.now().plusDays(nextDelay);
  }

  private LocalDateTime scheduleOnCreate(LearningEntry learningEntry) {
    return timeSource.now().plusDays(SCHEDULE_DELAY_ON_CREATE);
  }
}
