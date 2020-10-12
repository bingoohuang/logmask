package com.github.bingoohuang.logmask.log4j2;

import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.LogMask;
import com.github.bingoohuang.logmask.impl.Clz;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

@Plugin(name = "CustomeMasking", category = "Converter")
@ConverterKeys({"maskmsg", "mm"})
public class Layout extends LogEventPatternConverter {
  @Accessors private final String logmask = "logmask.xml";
  private Config config;

  protected Layout() {
    super("maskLayout", "maskStyle");

    config = Clz.loadXML(logmask, Config.class).setup();
  }

  @Override
  public void format(LogEvent event, StringBuilder outputMessage) {
    String message = event.getMessage().getFormattedMessage();
    outputMessage.append(LogMask.mask(config, message));
  }

  /**
   * Obtains an instance of pattern converter.
   *
   * @return instance of pattern converter.
   */
  public static Layout newInstance() {
    return new Layout();
  }
}
