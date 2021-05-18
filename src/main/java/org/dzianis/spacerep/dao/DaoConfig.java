package org.dzianis.spacerep.dao;

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
}
