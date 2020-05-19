package com.github.bingoohuang.logmask.logback;

import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.impl.Clz;
import java.util.Map;
import lombok.Data;
import lombok.val;

@Data
public class Layout extends ch.qos.logback.classic.PatternLayout {
  public static final ThreadLocal<Config> CONFIG = new InheritableThreadLocal<Config>();
  private String logmask = "logmask.xml";

  @Override
  public Map<String, String> getEffectiveConverterMap() {
    val m = super.getEffectiveConverterMap();
    m.put("m", Converter.class.getName());
    m.put("msg", Converter.class.getName());
    m.put("message", Converter.class.getName());

    return m;
  }

  @Override
  public void start() {
    CONFIG.set(Clz.loadXML(logmask, Config.class).setup());
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    CONFIG.remove();
  }
}
