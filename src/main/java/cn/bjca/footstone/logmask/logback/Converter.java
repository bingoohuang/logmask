package cn.bjca.footstone.logmask.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.bjca.footstone.logmask.Clz;
import cn.bjca.footstone.logmask.Config;
import cn.bjca.footstone.logmask.LogMask;
import lombok.val;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class Converter extends ClassicConverter {
  @Override
  public String convert(ILoggingEvent event) {
    Config conf = Layout.config.get();
    if (conf == null) {
      val c = (LoggerContext) LoggerFactory.getILoggerFactory();
      String logmaskFile = c.getProperty("logmask");
      if (logmaskFile == null) {
        logmaskFile = "logmask.xml";
      }

      conf = Clz.loadXML(logmaskFile, Config.class).setup();
      Layout.config.set(conf);
    }

    val msg = doConvert(conf, event);
    return LogMask.mask(conf, msg);
  }

  private String doConvert(Config conf, ILoggingEvent e) {
    val arr = e.getArgumentArray();
    if (arr == null) {
      return e.getFormattedMessage();
    }

    for (int i = 0; i < arr.length; i++) {
      arr[i] = LogMask.mask(conf, arr[i]);
    }

    return MessageFormatter.arrayFormat(e.getMessage(), arr).getMessage();
  }
}
