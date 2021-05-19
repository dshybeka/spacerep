package org.dzianis.spacerep.model;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.google.common.collect.ImmutableList;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.spacerep.protos.Mark;

class LearningEntryTest {

  @Test
  void testAddMark() {
    LearningEntry learningEntry =
        LearningEntry.builder().mark(Mark.newBuilder().setValue(4).build()).build();

    LearningEntry updatedLearningEntry =
        learningEntry.toBuilder().mark(Mark.newBuilder().setValue(5).build()).build();

    assertIterableEquals(
        ImmutableList.of(4, 5),
        updatedLearningEntry.getMarks().stream().map(Mark::getValue).collect(Collectors.toList()));
  }
}
