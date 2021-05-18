package org.dzianis.spacerep.dao;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.SpaceRepStateProto;

class LearningEntryDaoFileImpl implements LearningEntryDao {

  private final ConcurrentHashMap<Long, LearningEntryProto> data;
  private final StorageConnector storageConnector;

  LearningEntryDaoFileImpl(StorageConnector storageConnector) {
    this.storageConnector = storageConnector;
    data = new ConcurrentHashMap<>(storageConnector.readState().getEntriesMap());
  }

  @Override
  public void save(LearningEntryProto learningEntry) {
    data.compute(
        learningEntry.getId(),
        (id, learningEntryProto) -> {
          persistState();
          return learningEntry;
        });
  }

  @Override
  public Optional<LearningEntryProto> get(long id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public ImmutableList<LearningEntryProto> getAll() {
    return ImmutableList.copyOf(data.values());
  }

  @Override
  public void delete(long id) {
    data.compute(
        id,
        (id1, learningEntryProto) -> {
          data.remove(id1);
          persistState();
          return learningEntryProto;
        });
  }

  private void persistState() {
    storageConnector.persistState(toState());
  }

  private SpaceRepStateProto toState() {
    return SpaceRepStateProto.newBuilder().putAllEntries(data).build();
  }
}
