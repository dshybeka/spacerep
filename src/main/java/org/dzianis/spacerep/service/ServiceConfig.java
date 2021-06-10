package org.dzianis.spacerep.service;

import com.google.common.base.Converter;
import org.dzianis.spacerep.converter.LocalDateConverter;
import org.dzianis.spacerep.dao.DaoConfig.DatastoreBased;
import org.dzianis.spacerep.dao.LearningEntryDao;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/run/
@Configuration
@ComponentScan("org.dzianis.spacerep.service")
public class ServiceConfig {

  @Bean
  public LearningEntryService learningEntryService(
      Converter<LearningEntry, LearningEntryProto> learningEntryConverter,
      TimeSource timeSource,
      SchedulingService schedulingService,
      @DatastoreBased LearningEntryDao datastireLearningEntryDao,
      LocalDateConverter localDateConverter) {
    return new LearningEntryService(
        learningEntryConverter,
        timeSource,
        schedulingService,
        datastireLearningEntryDao,
        localDateConverter);
  }

  @Bean
  public TimeSource timeSource() {
    return new TimeSource();
  }

  @Bean
  public SchedulingService schedulingService(TimeSource timeSource) {
    return new SchedulingService(timeSource);
  }
}
