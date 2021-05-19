package org.dzianis.spacerep.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeSource {

  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  public LocalDate localDateNow() {
    return LocalDate.now();
  }

  public long timestampNow() {
    return now().toInstant(ZoneOffset.UTC).toEpochMilli();
  }
}
