package cn.bjca.footstone.logmask;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.Map;

/**
 * 用于生成子类的StringDesc
 *
 * @author bingoobjca
 */
public class StringDesc {
  @Setter public Map<String, RuleConfig> rulesMap;
  @Setter @Getter public Object bean;

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
