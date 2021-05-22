package org.dzianis.spacerep.converter;

import static java.time.ZoneOffset.UTC;

import com.google.protobuf.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.spacerep.protos.DateProto;

public class LocalDateConverter {

  public LocalDateTime toLocalDateTime(Timestamp timestamp) {
    return LocalDateTime.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos(), UTC);
  }

  public Timestamp safeToTimestamp(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return Timestamp.getDefaultInstance();
    }

    return Timestamp.newBuilder()
        .setSeconds(localDateTime.toInstant(UTC).getEpochSecond())
        .setNanos(localDateTime.toInstant(UTC).getNano())
        .build();
  }

  public LocalDate toLocalDate(DateProto dateProto) {
    return LocalDate.of(dateProto.getYear(), dateProto.getMonth(), dateProto.getDay());
  }

  public DateProto safeToDateProto(LocalDate localDate) {
    if (localDate == null) {
      return DateProto.getDefaultInstance();
    }

    return DateProto.newBuilder()
        .setYear(localDate.getYear())
        .setMonth(localDate.getMonthValue())
        .setDay(localDate.getDayOfMonth())
        .build();
  }
}
