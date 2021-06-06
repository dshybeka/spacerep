package org.dzianis.spacerep.converter;

import static org.dzianis.spacerep.dao.FieldNames.easinessFactorField;
import static org.dzianis.spacerep.dao.FieldNames.markField;

import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.common.base.Converter;
import org.spacerep.protos.EasinessFactor;
import org.spacerep.protos.Mark;

class EasinessFactorConverter extends Converter<EasinessFactor, FullEntity<IncompleteKey>> {

  @Override
  protected FullEntity<IncompleteKey> doForward(EasinessFactor easinessFactor) {
    return FullEntity.newBuilder()
        .set(easinessFactorField(EasinessFactor.VALUE_FIELD_NUMBER), easinessFactor.getValue())
        .set(easinessFactorField(EasinessFactor.DATE_FIELD_NUMBER), easinessFactor.getDate())
        .build();
  }

  @Override
  protected EasinessFactor doBackward(FullEntity<IncompleteKey> entity) {
    return EasinessFactor.newBuilder()
        .setValue(entity.getDouble(markField(Mark.VALUE_FIELD_NUMBER)))
        .setDate(Math.toIntExact(entity.getLong(markField(Mark.DATE_FIELD_NUMBER))))
        .build();
  }
}
