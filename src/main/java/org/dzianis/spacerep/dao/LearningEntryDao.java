package org.dzianis.spacerep.dao;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import org.spacerep.protos.LearningEntryProto;

public interface LearningEntryDao {

  LearningEntryProto insert(LearningEntryProto learningEntry);

  void update(LearningEntryProto learningEntry);

  Optional<LearningEntryProto> get(long id);

  ImmutableList<LearningEntryProto> getAll();

  void delete(long id);
}
