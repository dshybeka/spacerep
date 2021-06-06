package org.dzianis.spacerep.converter;

import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.common.base.Converter;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Mark;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

  @Bean
  public Converter<LearningEntry, LearningEntryProto> learningEntryConverter(
      LocalDateConverter localDateConverter) {
    return new LearningEntryConverter(localDateConverter);
  }

  @Bean
  public Converter<Mark, FullEntity<IncompleteKey>> markConverter() {
    return new MarkConverter();
  }

  @Bean
  public Converter<EasinessFactor, FullEntity<IncompleteKey>> easinessFactorConverter() {
    return new EasinessFactorConverter();
  }

  @Bean
  public DatastoreLearningEntryConverter datastoreLearningEntryConverter(
      LocalDateConverter localDateConverter,
      Converter<Mark, FullEntity<IncompleteKey>> markConverter,
      Converter<EasinessFactor, FullEntity<IncompleteKey>> easinessFactorConverter) {
    return new DatastoreLearningEntryConverter(
        localDateConverter, markConverter, easinessFactorConverter);
  }

  @Bean
  public LocalDateConverter localDateConverter() {
    return new LocalDateConverter();
  }
}
