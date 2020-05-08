package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MaskMessageConverter extends MessageConverter {
  private static Map<String, RuleConfig> rulesMap;
  private static boolean isInit = false;

  @Override
  public String convert(ILoggingEvent event) {
    if (!isInit) {
      init();
    }

    return doConvert(event);
  }

  private void init() {
    synchronized (MaskMessageConverter.class) {
      rulesMap = new HashMap<String, RuleConfig>(10);
      val propertyMap = ((LoggerContext) LoggerFactory.getILoggerFactory()).getCopyOfPropertyMap();

      for (Map.Entry<String, String> entry : propertyMap.entrySet()) {
        String ruleName = entry.getKey();
        String value = entry.getValue();
        if (!ruleName.endsWith("_REG")) {
          continue;
        }

        ruleName = ruleName.substring(0, ruleName.length() - "_REG".length());
        String replace = propertyMap.get(ruleName + "_REPLACE");
        if (replace == null) {
          replace = "---";
        }

        rulesMap.put(ruleName, new RuleConfig(value, replace));
      }
    }

    isInit = true;
  }

  private String doConvert(ILoggingEvent e) {
    Object[] arr = e.getArgumentArray();
    if (arr != null && arr.length > 0) {
      boolean maskMeRequired = false;

      for (Object obj : arr) {
        if (obj.getClass().isAnnotationPresent(Mask.class)) {
          maskMeRequired = true;
          break;
        }
      }

      if (maskMeRequired) {
        return MessageFormatter.arrayFormat(e.getMessage(), mask(arr).toArray()).getMessage();
      }
    }

    return e.getFormattedMessage();
  }

  private List<Object> mask(Object[] argumentArray) {
    val argumentList = new ArrayList<Object>();

    for (Object obj : argumentArray) {
      Class<?> aClass = obj.getClass();
      val stringDesc = StringDesc.create(aClass);
      if (stringDesc != null) {
        stringDesc.setBean(obj);
        stringDesc.setRulesMap(rulesMap);
        argumentList.add(stringDesc);
      } else {
        argumentList.add(obj);
      }
    }

    return argumentList;
  }
}
