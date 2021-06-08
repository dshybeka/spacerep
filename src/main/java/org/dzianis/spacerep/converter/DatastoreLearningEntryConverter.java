package org.dzianis.spacerep.converter;

import static java.util.stream.Collectors.toList;
import static org.dzianis.spacerep.dao.FieldNames.learningEntityField;
import static org.spacerep.protos.LearningEntryProto.ARCHIVED_AT_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.ATTEMPT_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.CHANGES_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.CREATED_AT_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.DELAY_IN_DAYS_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.EASINESS_FACTOR_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.ID_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.LAST_EASINESS_FACTOR_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.LAST_MARK_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.LINK_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.MARK_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.NAME_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.NOTES_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.SCHEDULED_FOR_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.STATUS_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.UPDATED_AT_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.UUID_FIELD_NUMBER;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Entity.Builder;
import com.google.cloud.datastore.EntityValue;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import com.google.common.base.Converter;
import java.util.List;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Mark;
import org.springframework.beans.factory.annotation.Autowired;

class DatastoreLearningEntryConverter extends Converter<LearningEntryProto, Entity> {

  private final LocalDateConverter localDateConverter;
  private final Converter<Mark, FullEntity<IncompleteKey>> markConverter;
  private final Converter<EasinessFactor, FullEntity<IncompleteKey>> easinessFactorConverter;

  @Autowired
  DatastoreLearningEntryConverter(
      LocalDateConverter localDateConverter,
      Converter<Mark, FullEntity<IncompleteKey>> markConverter,
      Converter<EasinessFactor, FullEntity<IncompleteKey>> easinessFactorConverter) {
    this.localDateConverter = localDateConverter;
    this.markConverter = markConverter;
    this.easinessFactorConverter = easinessFactorConverter;
  }

  @Override
  protected Entity doForward(LearningEntryProto learningEntry) {
    Key key = Key.fromUrlSafe(learningEntry.getUuid());
    Builder builder = Entity.newBuilder(key);

    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(learningEntry.getCreatedAt())) {
      builder.set(
          learningEntityField(CREATED_AT_FIELD_NUMBER),
          Timestamp.fromProto(learningEntry.getCreatedAt()));
    }
    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(learningEntry.getUpdatedAt())) {
      builder.set(
          learningEntityField(UPDATED_AT_FIELD_NUMBER),
          Timestamp.fromProto(learningEntry.getUpdatedAt()));
    }
    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(learningEntry.getArchivedAt())) {
      builder.set(
          learningEntityField(ARCHIVED_AT_FIELD_NUMBER),
          Timestamp.fromProto(learningEntry.getArchivedAt()));
    }

    builder
        .set(learningEntityField(ID_FIELD_NUMBER), learningEntry.getId())
        .set(learningEntityField(NAME_FIELD_NUMBER), learningEntry.getName())
        .set(learningEntityField(NOTES_FIELD_NUMBER), learningEntry.getNotes())
        .set(
            learningEntityField(CHANGES_FIELD_NUMBER),
            listOfStringsToValue(learningEntry.getChangesList()))
        .set(learningEntityField(ATTEMPT_FIELD_NUMBER), learningEntry.getAttempt())
        .set(learningEntityField(STATUS_FIELD_NUMBER), learningEntry.getStatusValue())
        .set(
            learningEntityField(SCHEDULED_FOR_FIELD_NUMBER),
            localDateConverter.toTimestamp(learningEntry.getScheduledFor()))
        .set(
            learningEntityField(MARK_FIELD_NUMBER), listOfMarksToValue(learningEntry.getMarkList()))
        .set(
            learningEntityField(EASINESS_FACTOR_FIELD_NUMBER),
            listOfEasinessFactorToValue(learningEntry.getEasinessFactorList()))
        .set(
            learningEntityField(LAST_MARK_FIELD_NUMBER),
            markConverter.convert(learningEntry.getLastMark()))
        .set(
            learningEntityField(LAST_EASINESS_FACTOR_FIELD_NUMBER),
            easinessFactorConverter.convert(learningEntry.getLastEasinessFactor()))
        .set(learningEntityField(LINK_FIELD_NUMBER), learningEntry.getLink())
        .set(learningEntityField(DELAY_IN_DAYS_FIELD_NUMBER), learningEntry.getDelayInDays())
        .set(learningEntityField(UUID_FIELD_NUMBER), learningEntry.getUuid());

    return builder.build();
  }

  @Override
  protected LearningEntryProto doBackward(Entity entity) {
    LearningEntryProto.Builder builder =
        LearningEntryProto.newBuilder()
            .setId(entity.getLong(learningEntityField(ID_FIELD_NUMBER)))
            .setName(entity.getString(learningEntityField(NAME_FIELD_NUMBER)))
            .setNotes(entity.getString(learningEntityField(NOTES_FIELD_NUMBER)))
            .addAllChanges(valueToListOfChanges(entity))
            .setAttempt(Math.toIntExact(entity.getLong(learningEntityField(ATTEMPT_FIELD_NUMBER))))
            .setStatusValue(
                Math.toIntExact(entity.getLong(learningEntityField(STATUS_FIELD_NUMBER))))
            .setScheduledFor(
                localDateConverter.fromTimestamp(
                    entity.getTimestamp(learningEntityField(SCHEDULED_FOR_FIELD_NUMBER))))
            .addAllMark(entityToListOfMarks(entity.getList(learningEntityField(MARK_FIELD_NUMBER))))
            .addAllEasinessFactor(
                entityToListOfEasinessFactors(
                    entity.getList(learningEntityField(EASINESS_FACTOR_FIELD_NUMBER))))
            .setLink(entity.getString(learningEntityField(LINK_FIELD_NUMBER)))
            .setDelayInDays(
                Math.toIntExact(entity.getLong(learningEntityField(DELAY_IN_DAYS_FIELD_NUMBER))))
            .setUuid(entity.getString(learningEntityField(UUID_FIELD_NUMBER)))
            .setLastMark(
                markConverter
                    .reverse()
                    .convert(entity.getEntity(learningEntityField(LAST_MARK_FIELD_NUMBER))))
            .setLastEasinessFactor(
                easinessFactorConverter
                    .reverse()
                    .convert(
                        entity.getEntity(learningEntityField(LAST_EASINESS_FACTOR_FIELD_NUMBER))));

    com.google.protobuf.Timestamp createdAt =
        getTimestampOrDefault(entity, learningEntityField(CREATED_AT_FIELD_NUMBER));
    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(createdAt)) {
      builder.setCreatedAt(createdAt);
    }
    com.google.protobuf.Timestamp updatedAt =
        getTimestampOrDefault(entity, learningEntityField(UPDATED_AT_FIELD_NUMBER));
    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(createdAt)) {
      builder.setUpdatedAt(updatedAt);
    }
    com.google.protobuf.Timestamp archivedAt =
        getTimestampOrDefault(entity, learningEntityField(ARCHIVED_AT_FIELD_NUMBER));
    if (!com.google.protobuf.Timestamp.getDefaultInstance().equals(createdAt)) {
      builder.setArchivedAt(archivedAt);
    }

    return builder.build();
  }

  private static com.google.protobuf.Timestamp getTimestampOrDefault(
      Entity entity, String fieldName) {
    if (entity.contains(fieldName)) {
      return entity.getTimestamp(fieldName).toProto();
    } else {
      return com.google.protobuf.Timestamp.getDefaultInstance();
    }
  }

  private List<Value<?>> listOfEasinessFactorToValue(List<EasinessFactor> values) {
    return values.stream()
        .map(easinessFactorConverter::convert)
        .map(v -> EntityValue.newBuilder(v).build())
        .collect(toList());
  }

  private List<Value<?>> listOfMarksToValue(List<Mark> values) {
    return values.stream()
        .map(markConverter::convert)
        .map(v -> EntityValue.newBuilder(v).build())
        .collect(toList());
  }

  private Iterable<Mark> entityToListOfMarks(List<Value<?>> markEntity) {
    return markEntity.stream()
        .map(mark -> markConverter.reverse().convert((FullEntity<IncompleteKey>) mark.get()))
        .collect(toList());
  }

  private Iterable<EasinessFactor> entityToListOfEasinessFactors(List<Value<?>> markEntity) {
    return markEntity.stream()
        .map(
            mark ->
                easinessFactorConverter.reverse().convert((FullEntity<IncompleteKey>) mark.get()))
        .collect(toList());
  }

  private static List<Value<?>> listOfStringsToValue(List<String> values) {
    return values.stream().map(value -> StringValue.newBuilder(value).build()).collect(toList());
  }

  private List<String> valueToListOfChanges(Entity entity) {
    List<Value<?>> value = entity.getList(learningEntityField(CHANGES_FIELD_NUMBER));
    return value.stream().map(v -> (String) v.get()).collect(toList());
  }
}
