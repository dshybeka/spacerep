package org.dzianis.spacerep.model;

import com.google.common.collect.ImmutableList;
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
  long id;

  String name;

  String notes;

  @Singular ImmutableList<String> changes;

  @Singular ImmutableList<Mark> marks;

  @Singular ImmutableList<EasinessFactor> easinessFactors;

  LocalDateTime createdAt;

  LocalDateTime updatedAt;

  LocalDateTime archivedAt;

  int attempt;

  Status status;

  LocalDateTime scheduledFor;

  public LearningEntry addMark(Mark mark) {
    return this.toBuilder().mark(mark).build();
  }

  public LearningEntry addChange(String change) {
    return this.toBuilder().change(change).build();
  }

  public LearningEntry addEasinessFactors(EasinessFactor easinessFactor) {
    return this.toBuilder().easinessFactor(easinessFactor).build();
  }
}
