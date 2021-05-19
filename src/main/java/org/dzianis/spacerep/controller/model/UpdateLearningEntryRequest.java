package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Singular;
import lombok.Value;
import org.spacerep.protos.Status;

@Value
@Builder(toBuilder = true)
public class UpdateLearningEntryRequest {
  long id;

  String name;

  @Default String notes = "";

  Status status;

  LocalDate scheduleFor;

  int markValue;

  @Singular List<String> links;
}
