package org.dzianis.spacerep.model;

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.Mark;
import org.spacerep.protos.Status;

@Value
@Builder(toBuilder = true)
public class LearningEntry {

  Long id;

  String name;

  String uuid;

  @Builder.Default String notes = "";

  @Singular ImmutableList<String> changes;

  @Singular ImmutableList<Mark> marks;

  @Singular ImmutableList<EasinessFactor> easinessFactors;

  String link;

  Mark lastMark;

  EasinessFactor lastEasinessFactor;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;

  LocalDateTime archivedAt;

  int attempt;

  Status status;

  LocalDate scheduledFor;

  int delayInDays;
}
