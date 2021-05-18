package org.dzianis.spacerep.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
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
@ContextConfiguration(classes = {DaoConfig.class, TestContextConfiguration.class}, loader = AnnotationConfigContextLoader.class)
class LearningEntryDaoFileImplTest {

  private static final LearningEntryProto ENTRY_1 =
      LearningEntryProto.newBuilder().setId(1).build();
  private static final LearningEntryProto ENTRY_2 =
      LearningEntryProto.newBuilder().setId(2).build();
  private static final LearningEntryProto ENTRY_3 =
      LearningEntryProto.newBuilder().setId(3).build();
  private static final LearningEntryProto ENTRY_4 =
      LearningEntryProto.newBuilder().setId(4).build();

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

  @BeforeEach
  void setUp() {
    learningEntryDao.save(ENTRY_1);
    learningEntryDao.save(ENTRY_2);
    learningEntryDao.save(ENTRY_3);
    learningEntryDao.save(ENTRY_4);
    Mockito.reset(storageConnector);
  }

  @Test
  void testGet() {
    assertEquals(ENTRY_2, learningEntryDao.get(2).get());
    assertEquals(ENTRY_4, learningEntryDao.get(4).get());
  }

  @Test
  void testSave() {
    LearningEntryProto expectedEntry = LearningEntryProto.newBuilder().setId(999).build();
    learningEntryDao.save(expectedEntry);

    assertEquals(expectedEntry, learningEntryDao.get(expectedEntry.getId()).get());
    learningEntryDao.delete(expectedEntry.getId());
    verify(storageConnector, times(2)).persistState(any());
  }

  @Test
  void testGetAll() {
    assertIterableEquals(
        ImmutableList.of(ENTRY_1, ENTRY_2, ENTRY_3, ENTRY_4), learningEntryDao.getAll());
  }

  @Test
  void testDelete() {
    LearningEntryProto expectedEntry = LearningEntryProto.newBuilder().setId(999).build();
    learningEntryDao.save(expectedEntry);

    assertEquals(expectedEntry, learningEntryDao.get(expectedEntry.getId()).get());
    learningEntryDao.delete(expectedEntry.getId());

    assertTrue(learningEntryDao.get(expectedEntry.getId()).isEmpty());
  }
}
