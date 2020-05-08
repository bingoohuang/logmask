package cn.bjca.footstone.logmask.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.bjca.footstone.logmask.LogMask;

public class Converter extends ClassicConverter {
  @Override
  public String convert(ILoggingEvent event) {
    return LogMask.mask(Layout.masksThreadLocal.get(), event.getFormattedMessage());
  }
}
