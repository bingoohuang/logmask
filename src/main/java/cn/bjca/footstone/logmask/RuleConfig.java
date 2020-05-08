package cn.bjca.footstone.logmask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleConfig {
  private String reg;
  private String replace;

  public String mask(String s) {
    return s.replaceAll(reg, replace);
  }
}
