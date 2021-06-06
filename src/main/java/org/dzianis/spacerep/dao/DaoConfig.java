package org.dzianis.spacerep.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

  @Bean
  public LearningEntryDao learningEntry(StorageConnector storageConnector) {
    return new LearningEntryDaoFileImpl(storageConnector);
  }

  @Bean
  public StorageConnector storageConnector() {
    return new StorageConnector();
  }

//  @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
//  @Retention(RetentionPolicy.RUNTIME)
//  @Qualifier
//  public @interface Datastore {}
}
