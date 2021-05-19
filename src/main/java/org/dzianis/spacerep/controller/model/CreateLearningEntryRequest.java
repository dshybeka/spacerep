package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Singular;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateLearningEntryRequest {
  String name;

  @Default String notes = "";

  LocalDate scheduleFor;

  Integer attempt;

  @Singular List<String> links;
}
