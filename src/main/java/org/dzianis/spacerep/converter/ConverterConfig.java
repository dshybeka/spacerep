package org.dzianis.spacerep.converter;

import com.google.common.base.Converter;
import org.dzianis.spacerep.model.LearningEntry;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

  @Bean
  public Converter<LearningEntry, LearningEntryProto> learningEntryConverter() {
    return new LearningEntryConverter();
  }
}
