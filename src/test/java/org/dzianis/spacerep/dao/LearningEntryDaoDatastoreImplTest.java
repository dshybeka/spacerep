package org.dzianis.spacerep.dao;

import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;
import static java.time.ZoneOffset.UTC;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import com.google.common.base.Converter;
import com.google.protobuf.Timestamp;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;
import org.dzianis.spacerep.converter.ConverterConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

// https://github.com/googleapis/java-datastore/blob/a510debc966c191defeb97260ea7e625e3812813/samples/snippets/src/test/java/com/google/datastore/snippets/ConceptsTest.java#L393-L398
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {ConverterConfig.class},
    loader = AnnotationConfigContextLoader.class)
class LearningEntryDaoDatastoreImplTest {

  private final LocalDatastoreHelper helper = LocalDatastoreHelper.create(1.0);

  @Autowired private Converter<LearningEntryProto, Entity> learningEntryProtoEntityConverter;

  @BeforeEach
  void setUp() throws IOException, InterruptedException {
    helper.start();

    Datastore datastore = helper.getOptions().toBuilder().setNamespace("test").build().getService();

    learningEntryDao =
        new LearningEntryDaoDatastoreImpl(datastore, learningEntryProtoEntityConverter);
  }

  @AfterEach
  void tearDown() throws InterruptedException, TimeoutException, IOException {
    helper.stop();
  }

  private LearningEntryDao learningEntryDao;

  private static final LocalDateTime NOW = LocalDateTime.now();
  private static final Timestamp TIMESTAMP =
      Timestamp.newBuilder()
          .setSeconds(NOW.toInstant(UTC).getEpochSecond())
          .setNanos(NOW.toInstant(UTC).getNano())
          .build();

  private static final DateProto DATE_PROTO =
      DateProto.newBuilder()
          .setYear(NOW.getYear())
          .setMonth(NOW.getMonthValue())
          .setDay(NOW.getDayOfMonth())
          .build();

  @Test
  void testCrud() {
    LearningEntryProto entity1 =
        LearningEntryProto.newBuilder()
            .setId(2)
            .setName("NAME")
            .setNotes("NOTES")
            .addChanges("CHANGE_1")
            .addChanges("CHANGE_2")
            .addMark(Mark.newBuilder().setValue(1).build())
            .addMark(Mark.newBuilder().setValue(2).build())
            .addEasinessFactor(EasinessFactor.newBuilder().setValue(1.1).build())
            .addEasinessFactor(EasinessFactor.newBuilder().setValue(2.2).build())
            .setCreatedAt(TIMESTAMP)
            .setUpdatedAt(TIMESTAMP)
            .setArchivedAt(TIMESTAMP)
            .setAttempt(1)
            .setStatus(Status.ARCHIVED)
            .setScheduledFor(DATE_PROTO)
            .setLastMark(Mark.newBuilder().setValue(5).build())
            .setLastEasinessFactor(EasinessFactor.newBuilder().setValue(2.32).build())
            .setDelayInDays(33)
            .build();

    LearningEntryProto inserted = learningEntryDao.insert(entity1);
    LearningEntryProto retrieved = learningEntryDao.get(entity1.getId()).get();

    assertThat(inserted)
        .ignoringRepeatedFieldOrder()
        .ignoringFields(
            LearningEntryProto.CREATED_AT_FIELD_NUMBER,
            LearningEntryProto.UPDATED_AT_FIELD_NUMBER,
            LearningEntryProto.ARCHIVED_AT_FIELD_NUMBER)
        .isEqualTo(retrieved);
  }
}
