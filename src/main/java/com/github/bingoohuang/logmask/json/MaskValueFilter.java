package com.github.bingoohuang.logmask.json;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.Mask;
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
  protected Config config;

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

      if (mask.empty()) {
        return null;
      }

      return config.ruleMask(value, mask.rule());
    } catch (Exception e) {
      log.warn("exception object class {} name {}", object.getClass(), name, e);
    }

    return value;
  }
}
