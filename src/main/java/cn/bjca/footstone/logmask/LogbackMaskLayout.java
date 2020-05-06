package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class LogbackMaskLayout extends PatternLayout {
  @Getter private final List<Mask> masks = new ArrayList<Mask>();

  public void addMask(Mask mask) {
    this.masks.add(mask.fix());
  }

  @Override
  public String doLayout(ILoggingEvent event) {
    return Masker.mask(masks, super.doLayout(event));
  }
}
