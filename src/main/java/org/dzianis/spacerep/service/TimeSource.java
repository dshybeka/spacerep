package org.dzianis.spacerep.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TimeSource {

  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  public long timestampNow() {
    return now().toInstant(ZoneOffset.UTC).toEpochMilli();
  }
}
