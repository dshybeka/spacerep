package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateLearningEntry {
  String name;

  @Default String notes = "";

  LocalDate scheduleFor;

  Integer attempt;

  String link;
}
