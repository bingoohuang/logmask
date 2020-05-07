package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackConverter extends ClassicConverter {
  @Override
  public String convert(ILoggingEvent event) {
    return Masker.mask(LogbackLayout.masksThreadLocal.get(), event.getFormattedMessage());
  }
}
