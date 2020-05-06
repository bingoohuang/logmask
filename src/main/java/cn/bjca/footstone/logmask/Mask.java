package cn.bjca.footstone.logmask;

import lombok.Data;
import lombok.val;

import java.util.regex.Pattern;

@Data
public class Mask {
  private String pattern;
  private Pattern compiled;

  private int leftKeep;
  private int rightKeep;
  private String mask;
  private boolean keepMasksLength;

  public Mask fix() {
    if (this.mask == null) {
      this.mask = "*+*";
    }

    this.compiled = Pattern.compile(pattern, Pattern.MULTILINE);

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

  public void fulfil(String k, String v) {
    if (k.equals("pattern")) {
      setPattern(v);
    } else if (k.equals("leftKeep")) {
      setLeftKeep(Integer.parseInt(v));
    } else if (k.equals("rightKeep")) {
      setRightKeep(Integer.parseInt(v));
    } else if (k.equals("mask")) {
      setMask(v);
    } else if (k.equals("keepMasksLength")) {
      setKeepMasksLength(Boolean.parseBoolean(v));
    }
  }
}
