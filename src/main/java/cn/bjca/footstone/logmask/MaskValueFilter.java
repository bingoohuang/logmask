package cn.bjca.footstone.logmask;

import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.Field;

@Slf4j
public class MaskValueFilter implements ValueFilter {
  @Override
  public Object process(Object object, String name, Object value) {
    if (value == null) {
      return null;
    }

    try {
      Field field = object.getClass().getDeclaredField(name);
      val mask = field.getAnnotation(Mask.class);
      if (mask == null) {
        return value;
      }

      if (mask.ignore()) {
        return null;
      }

      return MaskRule.mask(value, mask.rule());
    } catch (Exception e) {
      log.warn("exception object class {} name {}", object.getClass(), name, e);
    }

    return value;
  }
}
