package org.dzianis.spacerep.converter;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.base.Converter;
import com.google.protobuf.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.dzianis.spacerep.model.LearningEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spacerep.protos.DateProto;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Mark;
import org.spacerep.protos.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConverterConfig.class, loader = AnnotationConfigContextLoader.class)
class LearningEntryConverterTest {

  private static final LocalDateTime ARCHIVED_AT = LocalDateTime.now();
  private static final LocalDateTime UPDATED_AT = LocalDateTime.now();
  private static final LocalDateTime CREATED_AT = LocalDateTime.now();
  private static final LocalDate SCHEDULED_FOR = LocalDate.now();

  private static final Timestamp CREATED_AT_TIMESTAMP =
      Timestamp.newBuilder()
          .setSeconds(CREATED_AT.toInstant(UTC).getEpochSecond())
          .setNanos(CREATED_AT.toInstant(UTC).getNano())
          .build();
  private static final Timestamp UPDATED_AT_TIMESTAMP =
      Timestamp.newBuilder()
          .setSeconds(UPDATED_AT.toInstant(UTC).getEpochSecond())
          .setNanos(UPDATED_AT.toInstant(UTC).getNano())
          .build();
  private static final Timestamp ARCHIVED_AT_TIMESTAMP =
      Timestamp.newBuilder()
          .setSeconds(ARCHIVED_AT.toInstant(UTC).getEpochSecond())
          .setNanos(ARCHIVED_AT.toInstant(UTC).getNano())
          .build();
  private static final DateProto SCHEDULED_FOR_DATE =
      DateProto.newBuilder()
          .setYear(SCHEDULED_FOR.getYear())
          .setMonth(SCHEDULED_FOR.getMonthValue())
          .setDay(SCHEDULED_FOR.getDayOfMonth())
          .build();

  private static final LearningEntry LEARNING_ENTRY =
      LearningEntry.builder()
          .id(1L)
          .name("NAME")
          .notes("NOTES")
          .change("CHANGE_1")
          .change("CHANGE_2")
          .mark(Mark.newBuilder().setValue(1).build())
          .mark(Mark.newBuilder().setValue(2).build())
          .easinessFactor(EasinessFactor.newBuilder().setValue(1.1).build())
          .easinessFactor(EasinessFactor.newBuilder().setValue(2.2).build())
          .createdAt(CREATED_AT)
          .updatedAt(UPDATED_AT)
          .archivedAt(ARCHIVED_AT)
          .attempt(1)
          .status(Status.ARCHIVED)
          .scheduledFor(SCHEDULED_FOR)
          .lastMark(Mark.newBuilder().setValue(5).build())
          .lastEasinessFactor(EasinessFactor.newBuilder().setValue(2.32).build())
          .delayInDays(33)
          .build();

  private static final LearningEntryProto LEARNING_ENTRY_PROTO =
      LearningEntryProto.newBuilder()
          .setId(1)
          .setName("NAME")
          .setNotes("NOTES")
          .addChanges("CHANGE_1")
          .addChanges("CHANGE_2")
          .addMark(Mark.newBuilder().setValue(1).build())
          .addMark(Mark.newBuilder().setValue(2).build())
          .addEasinessFactor(EasinessFactor.newBuilder().setValue(1.1).build())
          .addEasinessFactor(EasinessFactor.newBuilder().setValue(2.2).build())
          .setCreatedAt(CREATED_AT_TIMESTAMP)
          .setUpdatedAt(UPDATED_AT_TIMESTAMP)
          .setArchivedAt(ARCHIVED_AT_TIMESTAMP)
          .setAttempt(1)
          .setStatus(Status.ARCHIVED)
          .setScheduledFor(SCHEDULED_FOR_DATE)
          .setLastMark(Mark.newBuilder().setValue(5).build())
          .setLastEasinessFactor(EasinessFactor.newBuilder().setValue(2.32).build())
          .setDelayInDays(33)
          .build();

  @Autowired private Converter<LearningEntry, LearningEntryProto> converter;

  @Test
  void testConvertForward() {
    assertEquals(LEARNING_ENTRY_PROTO, converter.convert(LEARNING_ENTRY));
  }

  @Test
  void testConvertBackward() {
    assertEquals(LEARNING_ENTRY, converter.reverse().convert(LEARNING_ENTRY_PROTO));
  }
}
