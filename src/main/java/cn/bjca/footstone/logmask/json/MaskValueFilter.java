package cn.bjca.footstone.logmask.json;

import cn.bjca.footstone.logmask.Mask;
import cn.bjca.footstone.logmask.Rules;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.Field;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaskValueFilter implements ValueFilter {
  protected Rules rules;

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

      return rules.mask(value, mask.rule());
    } catch (Exception e) {
      log.warn("exception object class {} name {}", object.getClass(), name, e);
    }

    return value;
  }
}
