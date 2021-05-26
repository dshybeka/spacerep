package org.dzianis.spacerep.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

  LocalDate schedule(LearningEntry learningEntry) {
    if (learningEntry.getScheduledFor() == null) {
      throw new IllegalArgumentException("Scheduled for date should not be null");
    }

    return scheduleOnUpdate(learningEntry);
  }

  /**
   * Next date is calculated using following formula: I(n):=I(n-1)*eFactor.
   *
   * @return
   */
  private LocalDate scheduleOnUpdate(LearningEntry learningEntry) {
    EasinessFactor easinessFactor = learningEntry.getLastEasinessFactor();

    LocalDate lastUpdated = learningEntry.getUpdatedAt().toLocalDate();
    LocalDate scheduledDate = learningEntry.getScheduledFor();

    long previousDelayDays = ChronoUnit.DAYS.between(lastUpdated, scheduledDate);
    long nextDelay =
        Math.min(
            Math.round(previousDelayDays * easinessFactor.getValue()), MAX_SCHEDULE_DELAY_DAYS);

    return moveToClosestWorkingDay(timeSource.localDateNow().plusDays(nextDelay));
  }

  public LocalDate scheduleOnCreate() {
    return moveToClosestWorkingDay(timeSource.localDateNow().plusDays(SCHEDULE_DELAY_ON_CREATE));
  }

  private static LocalDate moveToClosestWorkingDay(LocalDate date) {
    if (DayOfWeek.SATURDAY == date.getDayOfWeek()) {
      return date.plusDays(2);
    }
    if (DayOfWeek.SUNDAY == date.getDayOfWeek()) {
      return date.plusDays(1);
    }
    return date;
  }
}
