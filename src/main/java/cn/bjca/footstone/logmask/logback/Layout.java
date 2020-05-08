package cn.bjca.footstone.logmask.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.bjca.footstone.logmask.MaskConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Layout extends PatternLayout {
  public static ThreadLocal<List<MaskConfig>> masksThreadLocal;
  @Getter private final List<MaskConfig> masks = new ArrayList<MaskConfig>();

  public void addMask(MaskConfig mask) {
    this.masks.add(mask.fix());
  }

  @Override
  public void start() {
    masksThreadLocal =
        new InheritableThreadLocal<List<MaskConfig>>() {
          @Override
          protected List<MaskConfig> initialValue() {
            return masks;
          }
        };

    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    masksThreadLocal.remove();
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return super.doLayout(event);
  }
}
