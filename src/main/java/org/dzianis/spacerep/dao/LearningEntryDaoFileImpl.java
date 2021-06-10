package org.dzianis.spacerep.dao;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.SpaceRepStateProto;

class LearningEntryDaoFileImpl implements LearningEntryDao {

  private final AtomicLong maxId;
  private final ConcurrentHashMap<Long, LearningEntryProto> data;
  private final StorageConnector storageConnector;

  LearningEntryDaoFileImpl(StorageConnector storageConnector) {
    this.storageConnector = storageConnector;
    data = new ConcurrentHashMap<>(storageConnector.readState().getEntriesMap());
    maxId = new AtomicLong(data.keySet().stream().max(Comparator.naturalOrder()).orElse(0L));
  }

  public LearningEntryProto insert(LearningEntryProto learningEntry) {
    LearningEntryProto entityWithId;
    synchronized (this) {
      long nextId = maxId.incrementAndGet();

      entityWithId = learningEntry.toBuilder().setId(nextId).build();
      data.put(nextId, entityWithId);
    }
    persistState();
    return entityWithId;
  }

  @Override
  public void update(LearningEntryProto learningEntry) {
    data.computeIfPresent(learningEntry.getId(), (ignored, learningEntryProto) -> learningEntry);
    persistState();
  }

  @Override
  public Optional<LearningEntryProto> get(long id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public Optional<LearningEntryProto> get(String uuid) {
    throw new IllegalStateException("Unimplemented.");
  }

  @Override
  public ImmutableList<LearningEntryProto> getAll() {
    return ImmutableList.copyOf(data.values());
  }

  @Override
  public void delete(String id) {
    throw new IllegalStateException("Unimplemented.");
  }

  @Override
  public boolean containsName(String name) {
    return data.values().stream()
        .map(LearningEntryProto::getName)
        .map(String::toLowerCase)
        .anyMatch(entryName -> entryName.equals(name.toLowerCase()));
  }

  private void persistState() {
    storageConnector.persistState(toState());
  }

  private SpaceRepStateProto toState() {
    return SpaceRepStateProto.newBuilder().putAllEntries(data).build();
  }
}
