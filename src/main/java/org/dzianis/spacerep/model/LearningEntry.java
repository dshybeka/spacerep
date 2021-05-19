package org.dzianis.spacerep.model;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.Comparator;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.Mark;
import org.spacerep.protos.Status;

@Value
@Builder(toBuilder = true)
public class LearningEntry {

  private static final Comparator<EasinessFactor> EASINESS_FACTOR_BY_DATE_DESC =
      Comparator.comparing(EasinessFactor::getDate).reversed();

  private static final Comparator<Mark> MARK_BY_DATE_DESC =
      Comparator.comparing(Mark::getDate).reversed();

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

  public EasinessFactor getLatestEasinessFactor() {
    IllegalStateException possibleStateException =
        new IllegalStateException("Easiness factor is not set for entity: " + id);
    if (easinessFactors == null) {
      throw possibleStateException;
    }

    return easinessFactors.stream()
        .min(EASINESS_FACTOR_BY_DATE_DESC)
        .orElseThrow(() -> possibleStateException);
  }

  public Mark getLatestMark() {
    IllegalStateException possibleStateException =
        new IllegalStateException("Mark is not set for entity: " + id);
    if (easinessFactors == null) {
      throw possibleStateException;
    }

    return marks.stream().min(MARK_BY_DATE_DESC).orElseThrow(() -> possibleStateException);
  }
}
