package org.dzianis.spacerep.dao;

import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import org.dzianis.spacerep.converter.ConverterConfig;
import org.dzianis.spacerep.dao.DaoConfig.DatastoreBased;
import org.dzianis.spacerep.dao.DaoConfig.Local;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spacerep.protos.LearningEntryProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {ConverterConfig.class, DaoConfig.class},
    loader = AnnotationConfigContextLoader.class)
public class DatastoreTransferDao {

  @Autowired @Local private LearningEntryDao learningEntryDao;

  @Autowired @DatastoreBased private LearningEntryDao datastireLearningEntryDao;

  @Test
  void moveAllFromLocalToDatastore() {
    fail("Manual use only.");
    ImmutableList<LearningEntryProto> all = learningEntryDao.getAll();
    for (LearningEntryProto learningEntryProto : all) {
      datastireLearningEntryDao.insert(learningEntryProto);
    }
  }

  @Test
  void moveAllFromDatastoreToLocal() {
    fail("Manual use only.");
    ImmutableList<LearningEntryProto> all = learningEntryDao.getAll();
    for (LearningEntryProto learningEntryProto : all) {
      datastireLearningEntryDao.insert(learningEntryProto);
    }
  }
}
