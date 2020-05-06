package cn.bjca.footstone.logmask;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

public class Log4jMaskLayout extends PatternLayout {
  @Getter private String masks;
  @Getter private List<Mask> parsed = new ArrayList<Mask>();
  @Getter @Setter private String separate = " ";

  public void setMasks(String mask) {
    this.parsed.add(parseMask(mask));
  }

  private Mask parseMask(String mask) {
    val parts = mask.split(separate);
    val m = new Mask();

    for (val part : parts) {
      val kvs = part.split("=", 2);
      if (kvs.length == 2) {
        m.fulfil(kvs[0], kvs[1]);
      }
    }

    m.fix();

    return m;
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
            Masker.mask(parsed, event.getRenderedMessage()),
            ti != null ? ti.getThrowable() : null));
  }
}
