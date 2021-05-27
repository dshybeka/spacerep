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
            .scheduledFor(LocalDate.of(2021, 6, 8))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Valid parenthesis")
            .link("https://leetcode.com/problems/valid-parentheses/submissions/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 9))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Longest Palindromic Substring")
            .link("https://leetcode.com/problems/longest-palindromic-substring/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 9))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Merge two lists")
            .link("https://leetcode.com/problems/merge-two-sorted-lists/submissions/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 27))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Remove Duplicates from Sorted Array")
            .link("https://leetcode.com/problems/remove-duplicates-from-sorted-array/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 27))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("ZigZag Conversion")
            .link("https://leetcode.com/problems/zigzag-conversion/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 10))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("String to Integer (atoi)")
            .link("https://leetcode.com/problems/string-to-integer-atoi/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 10))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Search Insert Position")
            .link("https://leetcode.com/problems/search-insert-position/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 11))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Maximum Subarray")
            .link("https://leetcode.com/problems/maximum-subarray/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 4))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Length of Last Word")
            .link("https://leetcode.com/problems/length-of-last-word/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 14))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Container With Most Water")
            .link("https://leetcode.com/problems/container-with-most-water/")
            .attempt(3)
            .scheduledFor(LocalDate.of(2021, 6, 14))
            .delayInDays(21)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Add Binary")
            .link("https://leetcode.com/problems/add-binary/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 1))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Sqrt(x)")
            .link("https://leetcode.com/problems/sqrtx/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 8))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Integer to Roman")
            .link("https://leetcode.com/problems/integer-to-roman/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 26))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Remove Duplicates from Sorted List")
            .link("https://leetcode.com/problems/remove-duplicates-from-sorted-list/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 3))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Merge Sorted Array")
            .link("https://leetcode.com/problems/merge-sorted-array/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 27))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Convert Sorted Array to Binary Search Tree")
            .link("https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 27))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Balanced Binary Tree")
            .link("https://leetcode.com/problems/balanced-binary-tree/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 28))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Minimum Depth of Binary Tree")
            .link("https://leetcode.com/problems/minimum-depth-of-binary-tree/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 5, 31))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Path Sum")
            .link("https://leetcode.com/problems/path-sum/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 1))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("3Sum")
            .link("https://leetcode.com/problems/3sum/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 6, 9))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Pascal's Triangle")
            .link("https://leetcode.com/problems/pascals-triangle/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 7))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Best Time to Buy and Sell Stock")
            .link("https://leetcode.com/problems/best-time-to-buy-and-sell-stock/")
            .attempt(2)
            .scheduledFor(LocalDate.of(2021, 6, 7))
            .delayInDays(14)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Valid Palindrome")
            .link("https://leetcode.com/problems/valid-palindrome/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 5, 28))
            .delayInDays(7)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Pascal's Triangle II")
            .link("https://leetcode.com/problems/pascals-triangle-ii/submissions/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 5, 29))
            .delayInDays(7)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("3Sum Closest")
            .link("https://leetcode.com/problems/3sum-closest/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 5, 31))
            .delayInDays(7)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Linked List Cycle")
            .link("https://leetcode.com/problems/linked-list-cycle/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 6, 2))
            .delayInDays(7)
            .build());

    learningEntryService.createNew(
        CreateLearningEntry.builder()
            .name("Single Number")
            .link("https://leetcode.com/problems/single-number/")
            .attempt(1)
            .scheduledFor(LocalDate.of(2021, 6, 2))
            .delayInDays(7)
            .build());
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
