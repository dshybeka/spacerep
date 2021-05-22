package org.dzianis.spacerep.converter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WebLocalDateConverter implements Converter<String, LocalDate> {

  private static final List<String> SUPPORTED_FORMATS = Arrays.asList("yyyy-MM-dd", "dd-MM-yyyy");
  private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS =
      SUPPORTED_FORMATS.stream().map(DateTimeFormatter::ofPattern).collect(Collectors.toList());

  @Override
  public LocalDate convert(String s) {

    for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
      try {
        return LocalDate.parse(s, dateTimeFormatter);
      } catch (DateTimeParseException ex) {
        // deliberate empty block so that all parsers run
      }
    }

    throw new DateTimeException(
        String.format(
            "unable to parse (%s) supported formats are %s",
            s, String.join(", ", SUPPORTED_FORMATS)));
  }
}
