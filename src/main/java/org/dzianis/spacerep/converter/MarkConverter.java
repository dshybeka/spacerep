package org.dzianis.spacerep.converter;

import static org.dzianis.spacerep.dao.FieldNames.markField;

import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.common.base.Converter;
import org.spacerep.protos.Mark;

class MarkConverter extends Converter<Mark, FullEntity<IncompleteKey>> {

  @Override
  protected FullEntity<IncompleteKey> doForward(Mark mark) {
    return FullEntity.newBuilder()
        .set(markField(Mark.VALUE_FIELD_NUMBER), mark.getValue())
        .set(markField(Mark.DATE_FIELD_NUMBER), mark.getDate())
        .build();
  }

  @Override
  protected Mark doBackward(FullEntity<IncompleteKey> entity) {
    return Mark.newBuilder()
        .setValue(Math.toIntExact(entity.getLong(markField(Mark.VALUE_FIELD_NUMBER))))
        .setDate(Math.toIntExact(entity.getLong(markField(Mark.DATE_FIELD_NUMBER))))
        .build();
  }
}
