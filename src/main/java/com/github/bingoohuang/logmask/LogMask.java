package com.github.bingoohuang.logmask;

import com.github.bingoohuang.logmask.impl.Clz;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LogMask {
  public final String DEFAULT_MASK = "___";

  Config config = Clz.loadXML("logmask.xml", Config.class).setup();

  public void config(Config config) {
    LogMask.config = config.setup();
  }

  public String mask(Object obj) {
    return mask(config, obj);
  }

  public String mask(Config config, Object obj) {
    return config.mask(obj);
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
