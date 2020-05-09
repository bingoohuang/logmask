package cn.bjca.footstone.logmask.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.bjca.footstone.logmask.Config;
import cn.bjca.footstone.logmask.impl.Clz;
import lombok.Data;
import lombok.val;

import java.util.Map;

@Data
public class Layout extends PatternLayout {
  public static ThreadLocal<Config> config = new InheritableThreadLocal<Config>();
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
