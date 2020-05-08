package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.helpers.MessageFormatter;

@Slf4j
public class MaskRuleMessageConverter extends MessageConverter {
  @Override
  public String convert(ILoggingEvent event) {
    return doConvert(event);
  }

  private String doConvert(ILoggingEvent e) {
    val arr = e.getArgumentArray();
    if (arr == null) {
      return e.getFormattedMessage();
    }

    for (Object obj : arr) {
      if (obj.getClass().isAnnotationPresent(Mask.class)) {
        return MessageFormatter.arrayFormat(e.getMessage(), mask(arr)).getMessage();
      }
    }

    return e.getFormattedMessage();
  }

  private Object[] mask(Object[] arr) {
    val arguments = new Object[arr.length];

    for (int i = 0; i < arr.length; i++) {
      Object obj = arr[i];
      val desc = ToString.create(obj.getClass());
      if (desc != null) {
        desc.setBean(obj);
        obj = desc;
      }

      arguments[i] = obj;
    }

    return arguments;
  }
}
