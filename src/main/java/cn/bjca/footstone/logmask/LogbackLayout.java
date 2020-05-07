package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LogbackLayout extends PatternLayout {
  public static final ThreadLocal<List<Mask>> masksThreadLocal =
      new InheritableThreadLocal<List<Mask>>();
  @Getter private final List<Mask> masks = new ArrayList<Mask>();

  public void addMask(Mask mask) {
    this.masks.add(mask.fix());
  }

  @Override
  public void start() {
    masksThreadLocal.set(masks);
    super.start();
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return super.doLayout(event);
  }
}
