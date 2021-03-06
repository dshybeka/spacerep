package org.dzianis.spacerep.converter;

import com.google.common.base.Converter;
import java.util.Optional;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.LearningEntryProto.Builder;

class LearningEntryConverter extends Converter<LearningEntry, LearningEntryProto> {

  private final LocalDateConverter localDateConverter;

  LearningEntryConverter(LocalDateConverter localDateConverter) {
    this.localDateConverter = localDateConverter;
  }

  @Override
  protected LearningEntryProto doForward(LearningEntry learningEntry) {
    Builder builder =
        LearningEntryProto.newBuilder()
            .setName(learningEntry.getName())
            .setUuid(Optional.ofNullable(learningEntry.getUuid()).orElse(""))
            .setNotes(learningEntry.getNotes())
            .addAllChanges(learningEntry.getChanges())
            .addAllMark(learningEntry.getMarks())
            .setLink(learningEntry.getLink())
            .addAllEasinessFactor(learningEntry.getEasinessFactors())
            .setCreatedAt(localDateConverter.safeToTimestamp(learningEntry.getCreatedAt()))
            .setUpdatedAt(localDateConverter.safeToTimestamp(learningEntry.getUpdatedAt()))
            .setArchivedAt(localDateConverter.safeToTimestamp(learningEntry.getArchivedAt()))
            .setAttempt(learningEntry.getAttempt())
            .setScheduledFor(localDateConverter.safeToDateProto(learningEntry.getScheduledFor()))
            .setDelayInDays(learningEntry.getDelayInDays());
    if (learningEntry.getStatus() != null) {
      builder.setStatus(learningEntry.getStatus());
    }
    if (learningEntry.getLastMark() != null) {
      builder.setLastMark(learningEntry.getLastMark());
    }
    if (learningEntry.getLastEasinessFactor() != null) {
      builder.setLastEasinessFactor(learningEntry.getLastEasinessFactor());
    }
    return builder.build();
  }

  @Override
  protected LearningEntry doBackward(LearningEntryProto learningEntryProto) {
    return LearningEntry.builder()
        .name(learningEntryProto.getName())
        .uuid(learningEntryProto.getUuid())
        .notes(learningEntryProto.getNotes())
        .changes(learningEntryProto.getChangesList())
        .marks(learningEntryProto.getMarkList())
        .easinessFactors(learningEntryProto.getEasinessFactorList())
        .createdAt(
            learningEntryProto.hasCreatedAt()
                ? localDateConverter.toLocalDateTime(learningEntryProto.getCreatedAt())
                : null)
        .updatedAt(
            learningEntryProto.hasUpdatedAt()
                ? localDateConverter.toLocalDateTime(learningEntryProto.getUpdatedAt())
                : null)
        .archivedAt(
            learningEntryProto.hasArchivedAt()
                ? localDateConverter.toLocalDateTime(learningEntryProto.getArchivedAt())
                : null)
        .attempt(learningEntryProto.getAttempt())
        .status(learningEntryProto.getStatus())
        .scheduledFor(
            learningEntryProto.hasScheduledFor()
                ? localDateConverter.toLocalDate(learningEntryProto.getScheduledFor())
                : null)
        .lastMark(learningEntryProto.getLastMark())
        .lastEasinessFactor(learningEntryProto.getLastEasinessFactor())
        .link(learningEntryProto.getLink())
        .delayInDays(learningEntryProto.getDelayInDays())
        .build();
  }
}
