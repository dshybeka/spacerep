package org.dzianis.spacerep.dao;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.spacerep.protos.LearningEntryProto;

public interface LearningEntryDao {

  LearningEntryProto insert(LearningEntryProto learningEntry);

  void update(LearningEntryProto learningEntry);

  Optional<LearningEntryProto> get(long id);

  Optional<LearningEntryProto> get(String uuid);

  ImmutableList<LearningEntryProto> getAll();

  void delete(String id);

  boolean containsName(String name);
}
