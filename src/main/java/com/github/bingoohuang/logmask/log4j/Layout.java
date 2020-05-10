package com.github.bingoohuang.logmask.log4j;

import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.LogMask;
import com.github.bingoohuang.logmask.impl.Clz;
import lombok.experimental.Accessors;
import lombok.val;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class Layout extends PatternLayout {
  @Accessors
  private final String logmask = "logmask.xml";
  private Config config;

  /**
   * 所有的参数已经设置完毕后调用
   */
  @Override
  public void activateOptions() {
    super.activateOptions();

    config = Clz.loadXML(logmask, Config.class).setup();
  }

  @Override
  public String format(LoggingEvent event) {
    if (!(event.getMessage() instanceof String)) {
      return super.format(event);
    }

    val ti = event.getThrowableInformation();

    return super.format(
        new LoggingEvent(
            event.fqnOfCategoryClass,
            Logger.getLogger(event.getLoggerName()),
            event.timeStamp,
            event.getLevel(),
            LogMask.mask(config, event.getRenderedMessage()),
            ti != null ? ti.getThrowable() : null));
  }
}
