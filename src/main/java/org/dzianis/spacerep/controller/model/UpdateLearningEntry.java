package org.dzianis.spacerep.controller.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import org.spacerep.protos.Status;
import org.springframework.format.annotation.DateTimeFormat;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class UpdateLearningEntry {
  Long id;

  String name;

  @Default String notes = "";

  Status status;

  String uuid;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  LocalDate scheduledFor;

  Integer markValue;

  String link;

  Integer attempt;

  Integer delayInDays;
}
