package com.github.bingoohuang.logmask;

import lombok.Data;
import lombok.val;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.regex.Pattern;

@Data
@XmlRootElement
public class Config {
  private String rules = "logmask.rules";
  private List<Mask> mask;

  @XmlTransient private Rules parsedRules;

  public Config setup() {
    // 解析规则文件中的正则替换规则
    parsedRules = Rules.load(rules);

    for (val m : this.mask) {
      m.setup();
    }

    return this;
  }

  /**
   * 掩码配置类。
   *
   * @author bingoobjca
   */
  @Data
  public static class Mask {
    private String pattern;
    private String keys;
    private String mask;
    private String keep;

    @XmlTransient private Pattern compiled;
    @XmlTransient private int leftKeep;
    @XmlTransient private int rightKeep;

    public Mask setup() {
      if (mask == null) mask = LogMask.DEFAULT_MASK;
      if (pattern != null) compiled = Pattern.compile(pattern, Pattern.MULTILINE);

      return this;
    }

    public String replace(String s) {
      val maskLength = s.length() - leftKeep - rightKeep;

      if (s.length() < mask.length() || maskLength <= 0) {
        return mask;
      }

      val sb = new StringBuilder();

      if (leftKeep > 0) {
        sb.append(s, 0, leftKeep);
      }

      sb.append(mask);

      if (rightKeep > 0) {
        sb.append(s.substring(s.length() - rightKeep));
      }

      return sb.toString();
    }

    public void setKeep(String keep) {
      val parts = keep.split(",");
      if (parts.length == 1) {
        int v = Integer.parseInt(parts[0]);
        this.leftKeep = v;
        this.rightKeep = v;
      } else if (parts.length >= 2) {
        this.leftKeep = Integer.parseInt(parts[0]);
        this.rightKeep = Integer.parseInt(parts[1]);
      }
    }
  }
}
