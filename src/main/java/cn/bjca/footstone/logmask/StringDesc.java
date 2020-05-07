package cn.bjca.footstone.logmask;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.Map;

public class StringDesc<T> {
  @Setter public Map<String, RuleConfig> rulesMap;
  @Setter @Getter public T bean;

  public String mask(Object object, String ruleName) {
    if (object == null) {
      return null;
    }

    String s = object.toString();
    if (rulesMap != null && rulesMap.get(ruleName) != null) {
      val c = rulesMap.get(ruleName);
      return s.replaceAll(c.getReg(), c.getReplacement());
    }

    return "---";
  }
}
