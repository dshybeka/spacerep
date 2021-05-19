package org.dzianis.spacerep.service;

import com.google.common.collect.ImmutableList;
import org.dzianis.spacerep.controller.model.CreateLearningEntryRequest;
import org.dzianis.spacerep.controller.model.UpdateLearningEntryRequest;
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
  void testCreateNew() {
    CreateLearningEntryRequest entry1 =
        CreateLearningEntryRequest.builder()
            .name("Maximum Subarray")
            .notes("https://leetcode.com/problems/maximum-subarray/")
            .build();

    LearningEntryProto createdEntry = learningEntryService.createNew(entry1);

//    learningEntryService.update(
//        UpdateLearningEntryRequest.builder()
//            .id(createdEntry.getId())
//            .name(createdEntry.getName())
//            .notes(createdEntry.getNotes())
//            .markValue(4)
//            .build());
//    LearningEntryProto entity1AfterFirstUpdate = learningEntryService.get(createdEntry.getId());
//
//    learningEntryService.update(
//        UpdateLearningEntryRequest.builder()
//            .id(createdEntry.getId())
//            .name(createdEntry.getName())
//            .notes(createdEntry.getNotes())
//            .markValue(4)
//            .build());
//    LearningEntryProto entity1AfterFirstUpdate2 = learningEntryService.get(createdEntry.getId());
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
