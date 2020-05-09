package cn.bjca.footstone.logmask;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Value
@AllArgsConstructor
public class Rules {
  Map<String, RuleConfig> rules;

  public static Rules load(String rulesFile) {
    val rules = new HashMap<String, RuleConfig>(10);
    Rules r = new Rules(rules);

    Map<String, String> ruleLines;

    try {
      ruleLines = Clz.loadValues(rulesFile);
    } catch (Exception ex) {
      log.warn("failed to load {}", rulesFile);
      return r;
    }

    for (val entry : ruleLines.entrySet()) {
      String rule = entry.getKey();
      if (!rule.endsWith("_REG")) {
        continue;
      }

      rule = rule.substring(0, rule.length() - "_REG".length());
      String replace = ruleLines.get(rule + "_REPLACE");
      if (replace == null) {
        replace = LogMask.DEFAULT_MASK;
      }

      rules.put(rule, new RuleConfig(entry.getValue(), replace));
    }
    return r;
  }

  public String mask(Object obj, String rule) {
    if (obj == null) {
      return null;
    }

    if (rule == null) {
      return obj.toString();
    }

    val rc = rules.get(rule);
    if (rc != null) {
      return rc.mask(obj.toString());
    }

    return LogMask.DEFAULT_MASK;
  }
}
