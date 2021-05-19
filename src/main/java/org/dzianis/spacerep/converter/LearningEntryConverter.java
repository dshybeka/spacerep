package org.dzianis.spacerep.converter;

import static java.time.ZoneOffset.UTC;

import com.google.common.base.Converter;
import com.google.protobuf.Timestamp;
import java.time.LocalDateTime;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.LearningEntryProto;

class LearningEntryConverter extends Converter<LearningEntry, LearningEntryProto> {

  @Override
  protected LearningEntryProto doForward(LearningEntry learningEntry) {
    return LearningEntryProto.newBuilder()
        .setId(learningEntry.getId())
        .setName(learningEntry.getName())
        .setNotes(learningEntry.getNotes())
        .addAllChanges(learningEntry.getChanges())
        .addAllMark(learningEntry.getMarks())
        .addAllEasinessFactor(learningEntry.getEasinessFactors())
        .setCreatedAt(safeToTimestamp(learningEntry.getCreatedAt()))
        .setUpdatedAt(safeToTimestamp(learningEntry.getUpdatedAt()))
        .setArchivedAt(safeToTimestamp(learningEntry.getArchivedAt()))
        .setAttempt(learningEntry.getAttempt())
        .setStatus(learningEntry.getStatus())
        .setScheduledFor(safeToTimestamp(learningEntry.getScheduledFor()))
        .build();
  }

  @Override
  protected LearningEntry doBackward(LearningEntryProto learningEntryProto) {
    return LearningEntry.builder()
        .id(learningEntryProto.getId())
        .name(learningEntryProto.getName())
        .notes(learningEntryProto.getNotes())
        .changes(learningEntryProto.getChangesList())
        .marks(learningEntryProto.getMarkList())
        .easinessFactors(learningEntryProto.getEasinessFactorList())
        .createdAt(
            learningEntryProto.hasCreatedAt()
                ? toLocalDateTime(learningEntryProto.getCreatedAt())
                : null)
        .updatedAt(
            learningEntryProto.hasUpdatedAt()
                ? toLocalDateTime(learningEntryProto.getUpdatedAt())
                : null)
        .archivedAt(
            learningEntryProto.hasArchivedAt()
                ? toLocalDateTime(learningEntryProto.getArchivedAt())
                : null)
        .attempt(learningEntryProto.getAttempt())
        .status(learningEntryProto.getStatus())
        .scheduledFor(
            learningEntryProto.hasScheduledFor()
                ? toLocalDateTime(learningEntryProto.getScheduledFor())
                : null)
        .build();
  }

  private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
    return LocalDateTime.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos(), UTC);
  }

  private static Timestamp safeToTimestamp(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return Timestamp.getDefaultInstance();
    }

    return Timestamp.newBuilder()
        .setSeconds(localDateTime.toInstant(UTC).getEpochSecond())
        .setNanos(localDateTime.toInstant(UTC).getNano())
        .build();
  }
}