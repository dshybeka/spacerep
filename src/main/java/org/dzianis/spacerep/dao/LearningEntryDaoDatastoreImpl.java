package org.dzianis.spacerep.dao;

import static org.dzianis.spacerep.dao.FieldNames.learningEntityField;
import static org.spacerep.protos.LearningEntryProto.ID_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.NAME_FIELD_NUMBER;
import static org.spacerep.protos.LearningEntryProto.SCHEDULED_FOR_FIELD_NUMBER;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Optional;
import java.util.UUID;
import org.dzianis.spacerep.exception.EntityNotFoundException;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.beans.factory.annotation.Autowired;

// Auth
// https://cloud.google.com/docs/authentication/production

// https://github.com/GoogleCloudPlatform/java-docs-samples/blob/2e5996c68440134a79f1511c57529fa5cf987628/appengine-java8/datastore/src/test/java/com/example/appengine/EntitiesTest.java
// https://cloud.google.com/appengine/docs/standard/java/datastore/creating-entities

// https://cloud.google.com/datastore/docs/reference/libraries
//    https://cloud.google.com/datastore/docs/concepts/queries
class LearningEntryDaoDatastoreImpl implements LearningEntryDao {

  private static final String ENTITY_NAME = "learning_entity";

  private final Datastore datastore;
  private final Converter<LearningEntryProto, Entity> datastoreLearningEntryConverter;
  private final KeyFactory keyFactory;

  @Autowired
  LearningEntryDaoDatastoreImpl(
      Datastore dataStore, Converter<LearningEntryProto, Entity> datastoreLearningEntryConverter) {
    this.datastore = dataStore;
    this.datastoreLearningEntryConverter = datastoreLearningEntryConverter;
    this.keyFactory = datastore.newKeyFactory();
  }

  @Override
  public LearningEntryProto insert(LearningEntryProto learningEntry) {
    Key key = keyFactory.setKind(ENTITY_NAME).newKey(getNextUuid());
    LearningEntryProto entityWithId = learningEntry.toBuilder().setUuid(key.toUrlSafe()).build();

    Entity entry = datastoreLearningEntryConverter.convert(entityWithId);
    datastore.put(entry);
    return entityWithId;
  }

  @Override
  public void update(LearningEntryProto learningEntry) {
    datastore.put(datastoreLearningEntryConverter.convert(learningEntry));
  }

  @Override
  public Optional<LearningEntryProto> get(long id) {
    EntityQuery query =
        Query.newEntityQueryBuilder()
            .setKind(ENTITY_NAME)
            .setFilter(PropertyFilter.eq(learningEntityField(ID_FIELD_NUMBER), id))
            .build();

    QueryResults<Entity> results = datastore.run(query);

    if (results.hasNext()) {
      Entity next = results.next();
      return Optional.ofNullable(datastoreLearningEntryConverter.reverse().convert(next));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public ImmutableList<LearningEntryProto> getAll() {
    Builder<LearningEntryProto> builder = ImmutableList.builder();

    EntityQuery query =
        Query.newEntityQueryBuilder()
            .setKind(ENTITY_NAME)
            .setOrderBy(OrderBy.desc(learningEntityField(SCHEDULED_FOR_FIELD_NUMBER)))
            .build();

    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      LearningEntryProto learningEntryProto =
          datastoreLearningEntryConverter.reverse().convert(entity);
      builder.add(learningEntryProto);
    }

    return builder.build();
  }

  @Override
  public void delete(long id) {
    LearningEntryProto learningEntryProto =
        get(id)
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Learning entry with id " + id + " cannot be found."));
    datastore.delete(Key.fromUrlSafe(learningEntryProto.getUuid()));
  }

  @Override
  public boolean containsName(String name) {
    EntityQuery query =
        Query.newEntityQueryBuilder()
            .setKind(ENTITY_NAME)
            .setFilter(PropertyFilter.eq(learningEntityField(NAME_FIELD_NUMBER), name))
            .build();

    QueryResults<Entity> results = datastore.run(query);
    return results.hasNext();
  }

  private static String getNextUuid() {
    return UUID.randomUUID().toString();
  }
}
