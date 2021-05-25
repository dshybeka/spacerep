package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class CreateLearningEntry {
  String name;

  @Default String notes = "";

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate scheduleFor;

  Integer attempt;

  String link;
}
