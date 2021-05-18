package org.dzianis.spacerep.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.SpaceRepStateProto;

class StorageConnectorTest {

  StorageConnector storageConnector = new StorageConnector();

  @Test
  void testPersistAndReadState() {
    SpaceRepStateProto state1 =
        SpaceRepStateProto.newBuilder()
            .putEntries(1, LearningEntryProto.newBuilder().setId(123).setAttempt(1).build())
            .build();
    storageConnector.persistState(state1);
    SpaceRepStateProto state2 =
        SpaceRepStateProto.newBuilder()
            .putEntries(1, LearningEntryProto.newBuilder().setId(123).setAttempt(1).build())
            .putEntries(2, LearningEntryProto.newBuilder().setId(124).setAttempt(2).build())
            .build();
    storageConnector.persistState(state2);

    SpaceRepStateProto learningEntriesProto = storageConnector.readState();
    assertEquals(2, learningEntriesProto.getEntriesCount());

    // delete and read again
    learningEntriesProto.getEntriesMap().remove(123);
    storageConnector.persistState(learningEntriesProto);

    SpaceRepStateProto withDeletedEntry = storageConnector.readState();
    assertEquals(1, withDeletedEntry.getEntriesCount());
  }
}
