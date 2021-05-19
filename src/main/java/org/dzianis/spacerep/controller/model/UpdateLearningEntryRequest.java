package org.dzianis.spacerep.controller.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;
import org.spacerep.protos.Status;

@Value
@Builder(toBuilder = true)
public class UpdateLearningEntryRequest {
  long id;

  String name;

  String notes;

  Status status;

  LocalDateTime scheduleFor;

  int markValue;
}
