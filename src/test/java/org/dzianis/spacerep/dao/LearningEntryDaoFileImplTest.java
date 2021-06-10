package org.dzianis.spacerep.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import org.dzianis.spacerep.dao.LearningEntryDaoFileImplTest.TestContextConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.SpaceRepStateProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {DaoConfig.class, TestContextConfiguration.class},
    loader = AnnotationConfigContextLoader.class)
class LearningEntryDaoFileImplTest {

  @Configuration
  static class TestContextConfiguration {

    @Bean
    public StorageConnector storageConnector() {
      StorageConnector storageConnector = Mockito.mock(StorageConnector.class);
      when(storageConnector.readState()).thenReturn(SpaceRepStateProto.getDefaultInstance());

      return storageConnector;
    }
  }

  @Autowired private LearningEntryDao learningEntryDao;
  @Autowired private StorageConnector storageConnector;

  private LearningEntryProto entry1;
  private LearningEntryProto entry2;
  private LearningEntryProto entry3;
  private LearningEntryProto entry4;

  @BeforeEach
  void setUp() {
    ImmutableList<LearningEntryProto> storedEntries = learningEntryDao.getAll();
    for (LearningEntryProto storedEntry : storedEntries) {
//      learningEntryDao.delete(storedEntry.getId());
    }

    entry1 = learningEntryDao.insert(LearningEntryProto.newBuilder().setName("NAME_1").build());
    entry2 = learningEntryDao.insert(LearningEntryProto.newBuilder().setName("NAME_2").build());
    entry3 = learningEntryDao.insert(LearningEntryProto.newBuilder().setName("NAME_3").build());
    entry4 = learningEntryDao.insert(LearningEntryProto.newBuilder().setName("NAME_4").build());
    Mockito.reset(storageConnector);
  }

  @Test
  void testGet() {
    assertEquals(entry2, learningEntryDao.get(entry2.getId()).get());
    assertEquals(entry4, learningEntryDao.get(entry4.getId()).get());
  }

  @Test
  void testUpdate_existingRecords() {
    LearningEntryProto expectedEntry = LearningEntryProto.newBuilder().setId(999).build();
    expectedEntry = learningEntryDao.insert(expectedEntry);

    expectedEntry = expectedEntry.toBuilder().setName("NAME").build();
    learningEntryDao.update(expectedEntry);

    assertEquals(expectedEntry, learningEntryDao.get(expectedEntry.getId()).get());
//    learningEntryDao.delete(expectedEntry.getId());
    verify(storageConnector, times(3)).persistState(any());
  }

  @Test
  void testUpdate_nonExistingRecords() {
    LearningEntryProto expectedEntry = LearningEntryProto.newBuilder().setId(999).build();
    learningEntryDao.update(expectedEntry);

    assertTrue(learningEntryDao.get(expectedEntry.getId()).isEmpty());
  }

  @Test
  void testGetAll() {
    assertIterableEquals(
        ImmutableList.of(entry1, entry2, entry3, entry4), learningEntryDao.getAll());
  }

  @Test
  void testDelete() {
    LearningEntryProto expectedEntry = LearningEntryProto.newBuilder().setId(999).build();
    expectedEntry = learningEntryDao.insert(expectedEntry);

    assertEquals(expectedEntry, learningEntryDao.get(expectedEntry.getId()).get());
//    learningEntryDao.delete(expectedEntry.getId());

    assertTrue(learningEntryDao.get(expectedEntry.getId()).isEmpty());
  }
}
