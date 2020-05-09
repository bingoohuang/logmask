package cn.bjca.footstone.logmask.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.bjca.footstone.logmask.Clz;
import cn.bjca.footstone.logmask.Config;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Layout extends PatternLayout {
  public static ThreadLocal<Config> config = new InheritableThreadLocal<Config>();
  private String logmask = "logmask.xml";

  public static final Map<String, String> defaultConverterMap;

  static {
    // clone default defaultConverterMap
    defaultConverterMap = new HashMap<String, String>(PatternLayout.defaultConverterMap);
    defaultConverterMap.put("m", Converter.class.getName());
    defaultConverterMap.put("msg", Converter.class.getName());
    defaultConverterMap.put("message", Converter.class.getName());
  }

  @Override
  public Map<String, String> getDefaultConverterMap() {
    return defaultConverterMap;
  }

  @Override
  public void start() {
    config.set(Clz.loadXML(logmask, Config.class).setup());

    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    config.remove();
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return super.doLayout(event);
  }
}
