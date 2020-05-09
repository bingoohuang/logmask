package cn.bjca.footstone.logmask;

import cn.bjca.footstone.logmask.impl.Clz;
import cn.bjca.footstone.logmask.impl.Str;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Value
@AllArgsConstructor
public class Rules {
  Map<String, RuleConfig> ruleConfigMap;

  public static Rules load(String rulesFile) {
    val rules = new HashMap<String, RuleConfig>(10);
    val r = new Rules(rules);

    Map<String, String> ruleLines;

    try {
      ruleLines = Clz.loadValues(rulesFile);
    } catch (Exception ex) {
      log.warn("failed to load {}", rulesFile);
      return r;
    }

    for (val entry : ruleLines.entrySet()) {
      val rule = entry.getKey();
      if (!rule.endsWith("_REG")) {
        continue;
      }

      val ruleName = Str.stripPostfix(rule, "_REG");
      val replace = Str.getOrDefault(ruleLines, ruleName + "_REPLACE", LogMask.DEFAULT_MASK);
      rules.put(ruleName, new RuleConfig(entry.getValue(), replace));
    }

    return r;
  }

  public String mask(Object obj, String rule) {
    if (obj == null) return null;
    if (rule == null) return obj.toString();

    val rc = ruleConfigMap.get(rule);
    return rc != null ? rc.mask(obj.toString()) : LogMask.DEFAULT_MASK;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RuleConfig {
    private String reg;
    private String replace;

    public String mask(String s) {
      return s.replaceAll(reg, replace);
    }
  }
}
