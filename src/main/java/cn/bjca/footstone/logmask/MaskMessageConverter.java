package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MaskMessageConverter extends MessageConverter {
  private static Map<String, RuleConfig> rulesMap;
  private static boolean isInit = false;

  @Override
  public String convert(ILoggingEvent event) {
    if (!isInit) {
      init();
      isInit = true;
    }

    return doConvert(event);
  }

  private synchronized void init() {
    rulesMap = new HashMap<String, RuleConfig>(10);
    val context = ((LoggerContext) LoggerFactory.getILoggerFactory()).getCopyOfPropertyMap();

    for (val entry : context.entrySet()) {
      String rule = entry.getKey();
      if (!rule.endsWith("_REG")) {
        continue;
      }

      rule = rule.substring(0, rule.length() - "_REG".length());
      String replace = context.get(rule + "_REPLACE");
      if (replace == null) {
        replace = "___";
      }

      rulesMap.put(rule, new RuleConfig(entry.getValue(), replace));
    }
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

  private Object[] mask(Object[] argumentArray) {
    val arguments = new Object[argumentArray.length];

    for (int i = 0; i < argumentArray.length; i++) {
      Object obj = argumentArray[i];
      val desc = ToString.create(obj.getClass());
      if (desc != null) {
        desc.setBean(obj);
        desc.setRulesMap(rulesMap);
        obj = desc;
      }

      arguments[i] = obj;
    }

    return arguments;
  }
}
