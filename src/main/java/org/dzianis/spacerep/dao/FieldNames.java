package org.dzianis.spacerep.dao;

import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.LearningEntryProto;
import org.spacerep.protos.Mark;

public class FieldNames {

  public static String learningEntityField(int fieldNumber) {
    return LearningEntryProto.getDescriptor().findFieldByNumber(fieldNumber).getName();
  }

  public static String markField(int fieldNumber) {
    return Mark.getDescriptor().findFieldByNumber(fieldNumber).getName();
  }

  public static String easinessFactorField(int fieldNumber) {
    return EasinessFactor.getDescriptor().findFieldByNumber(fieldNumber).getName();
  }

  private FieldNames() {}
}
