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
  private static Map<String, RuleConfig> rulesMasp;
  private static boolean isInit = false;

  @Override
  public String convert(ILoggingEvent event) {
    if (!isInit) init();

    return doConvert(event);
  }

  private void init() {
    synchronized (MaskMessageConverter.class) {
      rulesMasp = new HashMap<String, RuleConfig>();
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

        rulesMasp.put(ruleName, new RuleConfig(value, replace));
      }
    }
    isInit = true;
  }

  private String doConvert(ILoggingEvent event) {
    boolean maskMeRequired = false;
    Object[] argumentArray = event.getArgumentArray();
    if (argumentArray != null && argumentArray.length > 0) {
      for (Object obj : argumentArray) {
        MaskMe annotation = obj.getClass().getAnnotation(MaskMe.class);
        if (annotation != null) {
          maskMeRequired = true;
          break;
        }
      }
      if (maskMeRequired) {
        List<Object> maskedObjs = mask(argumentArray);
        return MessageFormatter.arrayFormat(event.getMessage(), maskedObjs.toArray()).getMessage();
      }
    }

    return event.getFormattedMessage();
  }

  private List<Object> mask(Object[] argumentArray) {
    List<Object> argumentList = new ArrayList<Object>();
    for (Object obj : argumentArray) {
      Class<?> aClass = obj.getClass();
      MaskMe annotation = aClass.getAnnotation(MaskMe.class);
      StringDesc stringDesc = StringDescCreator.createStringDesc(aClass);
      if (stringDesc != null) {
        stringDesc.setBean(obj);
        stringDesc.setRulesMap(rulesMasp);
        argumentList.add(stringDesc);
      } else {
        argumentList.add(obj);
      }
    }
    return argumentList;
  }
}
