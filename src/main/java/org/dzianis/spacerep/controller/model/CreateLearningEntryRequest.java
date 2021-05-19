package org.dzianis.spacerep.controller.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CreateLearningEntryRequest {
  String name;

  String notes;
}
