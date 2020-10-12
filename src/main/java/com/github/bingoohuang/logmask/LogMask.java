package com.github.bingoohuang.logmask;

import com.github.bingoohuang.logmask.impl.Clz;
import com.github.bingoohuang.logmask.impl.ToString;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class LogMask {
  public final String DEFAULT_MASK = "___";

  Config config = Clz.loadXML("logmask.xml", Config.class).setup();

  public String mask(Object obj) {
    return mask(config, obj);
  }

  public String mask(Config config, Object obj) {
    if (obj == null) {
      return null;
    }

    if (obj.getClass().isAnnotationPresent(Mask.class)) {
      val desc = ToString.create(obj.getClass());
      if (desc != null) {
        desc.setBean(obj);
        desc.setConf(config);

        return desc.toString();
      }
    }

    return mask(config, obj.toString());
  }

  public String mask(Config config, String src) {
    return config.mask(src);
  }

  public boolean isQuoteChar(char c) {
    return c == '"' || c == '\\';
  }

  public boolean isBoundaryChar(char l, char r) {
    return isBoundaryChar(l) && isBoundaryChar(r);
  }

  public boolean isBoundaryChar(char c) {
    return !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9');
  }
}
