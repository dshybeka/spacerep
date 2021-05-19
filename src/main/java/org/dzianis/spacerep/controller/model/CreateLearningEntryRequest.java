package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateLearningEntryRequest {
  String name;

  String notes;

  LocalDate scheduleFor;

  Integer attempt;
}
