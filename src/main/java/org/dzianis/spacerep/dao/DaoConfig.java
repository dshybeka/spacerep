package org.dzianis.spacerep.dao;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.common.base.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

  @Bean
  @Local
  public LearningEntryDao learningEntry(StorageConnector storageConnector) {
    return new LearningEntryDaoFileImpl(storageConnector);
  }

  @Bean
  @DatastoreBased
  public LearningEntryDao datastoreLearningEntry(
      Datastore datastore, Converter<LearningEntryProto, Entity> learningEntryConverter) {
    return new LearningEntryDaoDatastoreImpl(datastore, learningEntryConverter);
  }

  @Bean
  public Datastore datastore() {
    return DatastoreOptions.getDefaultInstance().getService();
  }

  @Bean
  public StorageConnector storageConnector() {
    return new StorageConnector();
  }

  @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  public @interface DatastoreBased {}

  @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Qualifier
  public @interface Local {}
}
