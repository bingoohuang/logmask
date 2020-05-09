package cn.bjca.footstone.logmask;

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
    if (this.rules != null) {
      // 解析规则文件中的正则替换规则
      this.parsedRules = Rules.load(this.rules);
    }

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

    @XmlTransient private Pattern compiled;

    private int leftKeep;
    private int rightKeep;
    private String keep;
    private String mask;
    private boolean keepMasksLength;
    private boolean boundary;
    private String keys;

    public Mask setup() {
      if (this.mask == null) {
        this.mask = LogMask.DEFAULT_MASK;
      }

      if (this.pattern != null) {
        String p = pattern;
        if (boundary) {
          p = "\\b" + pattern + "\\b";
        }
        this.compiled = Pattern.compile(p, Pattern.MULTILINE);
      }

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

      if (keepMasksLength) {
        for (int i = 0, j = maskLength / mask.length(); i < j; i++) {
          sb.append(mask);
        }
      } else {
        sb.append(mask);
      }

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
