package cn.bjca.footstone.logmask;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MaskRule {
  public static final Map<String, RuleConfig> rulesMap;
  public static final String DEFAULT_MASK = "___";

  static {
    rulesMap = new HashMap<String, RuleConfig>(10);
    init();
  }

  static void init() {
    Map<String, String> rules;

    val file = "mask-rule.txt";
    try {
      rules = Clz.loadValues(file);
    } catch (Exception ex) {
      log.warn("failed to load {}", file);
      return;
    }

    for (val entry : rules.entrySet()) {
      String rule = entry.getKey();
      if (!rule.endsWith("_REG")) {
        continue;
      }

      rule = rule.substring(0, rule.length() - "_REG".length());
      String replace = rules.get(rule + "_REPLACE");
      if (replace == null) {
        replace = DEFAULT_MASK;
      }

      rulesMap.put(rule, new RuleConfig(entry.getValue(), replace));
    }
  }

  public static String mask(Object obj, String rule) {
    if (obj == null) {
      return null;
    }

    if (rule == null) {
      return obj.toString();
    }

    RuleConfig rc = MaskRule.rulesMap.get(rule);
    if (rc != null) {
      return rc.mask(obj.toString());
    }

    return MaskRule.DEFAULT_MASK;
  }
}
