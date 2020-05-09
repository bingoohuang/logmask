package cn.bjca.footstone.logmask.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class MaskSerializer extends JsonSerializer<String> implements ContextualSerializer {
  private Mask mask;

  @Override
  public JsonSerializer<String> createContextual(SerializerProvider sp, BeanProperty bp) {
    mask = bp.getAnnotation(Mask.class);

    return this;
  }

  @Override
  public void serialize(String o, JsonGenerator jg, SerializerProvider sp) throws IOException {
    if (mask == null) {
      jg.writeObject(o);
      return;
    }

    if (mask.ignore()) {
      return;
    }

    //    jg.writeObject(Rules.mask(o, mask.rule()));
  }
}
