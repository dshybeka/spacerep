package org.dzianis.spacerep.service;

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import org.dzianis.spacerep.controller.model.CreateLearningEntry;
import org.dzianis.spacerep.converter.ConverterConfig;
import org.dzianis.spacerep.dao.DaoConfig;
import org.dzianis.spacerep.dao.LearningEntryDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {ConverterConfig.class, ServiceConfig.class, DaoConfig.class},
    loader = AnnotationConfigContextLoader.class)
class LearningEntryServiceIntegrationTest {

  //  @MockBean LearningEntryDao learningEntryDao;
  @Autowired LearningEntryDao learningEntryDao;

  @Autowired private LearningEntryService learningEntryService;

  @Test
  void testCreateFew() {
    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Longest Substring Without Repeating Characters")
            .link("https://leetcode.com/problems/longest-substring-without-repeating-characters")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 8))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Valid parenthesis")
            .link("https://leetcode.com/problems/valid-parentheses/submissions/")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 9))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Longest Palindromic Substring")
            .link("https://leetcode.com/problems/longest-palindromic-substring/")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 9))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Merge two lists")
            .link("https://leetcode.com/problems/merge-two-sorted-lists/submissions/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 27))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Remove Duplicates from Sorted Array")
            .link("https://leetcode.com/problems/remove-duplicates-from-sorted-array/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 27))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("ZigZag Conversion")
            .link("https://leetcode.com/problems/zigzag-conversion/")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 10))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("String to Integer (atoi)")
            .link("https://leetcode.com/problems/string-to-integer-atoi/")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 10))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Search Insert Position")
            .link("https://leetcode.com/problems/search-insert-position/")
            .attempt(3)
            .scheduleFor(LocalDate.of(2021, 6, 11))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Maximum Subarray")
            .link("https://leetcode.com/problems/maximum-subarray/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 6, 4))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Length of Last Word")
            .link("https://leetcode.com/problems/length-of-last-word/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 24))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Container With Most Water")
            .link("https://leetcode.com/problems/container-with-most-water/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 24))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Add Binary")
            .link("https://leetcode.com/problems/add-binary/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 6, 1))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Sqrt(x)")
            .link("https://leetcode.com/problems/sqrtx/")
            .attempt(1)
            .scheduleFor(LocalDate.of(2021, 5, 25))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Integer to Roman")
            .link("https://leetcode.com/problems/integer-to-roman/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 26))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Remove Duplicates from Sorted List")
            .link("https://leetcode.com/problems/remove-duplicates-from-sorted-list/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 6, 3))
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Merge Sorted Array")
            .link("https://leetcode.com/problems/merge-sorted-array/")
            .attempt(2)
            .scheduleFor(LocalDate.of(2021, 5, 27))
            .build());

//    todo: add last offset to the model.
  }

  @Test
  void testCreateNew() {
    CreateLearningEntry entry1 =
        CreateLearningEntry.builder()
            .name("Maximum Subarray")
            .notes("https://leetcode.com/problems/maximum-subarray/")
            .build();

    LearningEntryProto createdEntry = learningEntryService.createNew(entry1);

    //    learningEntryService.update(
    //        UpdateLearningEntry.builder()
    //            .id(createdEntry.getId())
    //            .name(createdEntry.getName())
    //            .notes(createdEntry.getNotes())
    //            .markValue(4)
    //            .build());
    //    LearningEntryProto entity1AfterFirstUpdate =
    // learningEntryService.get(createdEntry.getId());
    //
    //    learningEntryService.update(
    //        UpdateLearningEntry.builder()
    //            .id(createdEntry.getId())
    //            .name(createdEntry.getName())
    //            .notes(createdEntry.getNotes())
    //            .markValue(4)
    //            .build());
    //    LearningEntryProto entity1AfterFirstUpdate2 =
    // learningEntryService.get(createdEntry.getId());
    //
    //    System.out.println("entity1AfterFirstUpdate " + entity1AfterFirstUpdate);
    //    System.out.println("entity1AfterFirstUpdate2 " + entity1AfterFirstUpdate2);
  }

  @Test
  void getEntries() {
    ImmutableList<LearningEntryProto> all = learningEntryDao.getAll();

    System.out.println("all " + all);

    for (LearningEntryProto learningEntryProto : all) {
      learningEntryDao.delete(learningEntryProto.getId());
    }
  }
}
