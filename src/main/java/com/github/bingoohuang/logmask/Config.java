package com.github.bingoohuang.logmask;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Data
@XmlRootElement
public class Config {
  private List<Mask> mask;

  @XmlTransient private Map<String, Mask> rules = new HashMap<String, Mask>(10);

  public Config setup() {
    for (val m : this.mask) {
      m.setup(rules);
    }

    return this;
  }

  public String mask(Object obj) {
    if (obj == null) return null;

    String src = obj.toString();
    for (val m : this.mask) {
      if (m.getRule() == null) {
        src = m.mask(src);
      }
    }

    return src;
  }

  public String ruleMask(Object obj, String rule) {
    if (obj == null) return null;
    if (rule == null) return obj.toString();

    val rc = rules.get(rule);
    return rc != null ? rc.mask(obj.toString()) : LogMask.DEFAULT_MASK;
  }

  @Data
  @Slf4j
  public static class Mask {
    // 规则名称，在JSON序列化与toString序列化JavaBean时的@Mask中的rule规则
    private String rule;

    // 正则表达式
    private String pattern;
    // JSON,toString(key=value, key:value)等形式的key列表，以空格分隔
    private String keys;

    // 加密替换，设置后，优先级 3
    private String encrypt;

    // 直接正则替换，设置后，优先级2
    private String replace;

    // 替换掩码，优先级3，默认值___
    private String mask;

    // 脱敏后的数据保留, 格式. (null) x x.y x.y.z
    // 此字段将在setter方法中解析成 leftKeep.rightKeep.maskLength

    // 1. 不设置 表示不保留首位的部分原始字符
    // 2. x 表示保留首位的长度为x的原始字符, 等价于x.x
    // 3. x.y 表示保留首位的长度为x的原始字符，等价于x.z.-2
    // 4. x.y.z z表示掩码长度  -2：直接是掩码长度 -1: 原始长度 >= 0: 指定长度

    // eg.
    // 3: 首尾各保留3位原字符，例如 abcdefg -> abc***efg
    // 0,3: 尾部留3位原字符，例如 abcdefg -> ***efg
    // 3,0: 首部留3位原字符，例如 abcdefg -> abc***
    private String keep;

    @XmlTransient private Pattern compiled;
    @XmlTransient private int leftKeep;
    @XmlTransient private int rightKeep;
    /** 掩码长度: -2：直接是掩码长度 -1: 原始长度 >= 0: 指定长度 */
    @XmlTransient private int maskLength = -2;

    public Mask setup(Map<String, Mask> rules) {
      if (mask == null) mask = LogMask.DEFAULT_MASK;
      if (pattern != null) compiled = Pattern.compile(pattern, Pattern.MULTILINE);

      if (rule != null && rules != null) {
        if (rules.containsKey(rule)) {
          log.warn("rule {} duplicated, the last will overwrite previous", rule);
        }

        rules.put(rule, this);
      }

      return this;
    }

    public String mask(String s) {
      return keysMask(patternMask(s));
    }

    private String patternMask(String src) {
      if (compiled == null) return src;

      val m = compiled.matcher(src);

      if (replace != null) {
        return m.replaceAll(replace);
      }

      int start = 0;
      val sb = new StringBuilder();

      while (m.find()) {
        if (m.start() > 0) {
          sb.append(src, start, m.start());
        }

        sb.append(this.maskResult(m.group()));
        start = m.end();
      }

      if (start > 0) {
        if (start < src.length()) {
          sb.append(src.substring(start));
        }

        return sb.toString();
      }

      return src;
    }

    private String keysMask(String src) {
      if (keys == null) return src;

      String dest = src;

      for (val key : keys.split("\\s+")) {
        dest = keyMask(key, dest);
      }

      return dest;
    }

    private String keyMask(String key, String src) {
      int start = src.indexOf(key);
      if (start < 0) {
        return src;
      }

      val sb = new StringBuilder();
      start = 0;

      do {
        int next = src.indexOf(key, start);
        if (next < 0) {
          sb.append(src.substring(start));
          break;
        }

        sb.append(src, start, next + key.length());

        char leftChar = ' ';
        char rightChar = ' ';
        if (next > 0) {
          leftChar = src.charAt(next - 1);
        }
        if (next + key.length() < src.length()) {
          rightChar = src.charAt(next + key.length());
        }

        if (!LogMask.isBoundaryChar(leftChar, rightChar)) {
          start = next + key.length();

          continue;
        }

        // key:value or key=value
        if (LogMask.isBlankChar(leftChar) && (rightChar == ':' || rightChar == '=')) {
          sb.append(rightChar);

          int valueEnd = src.indexOf(", ", next + key.length());
          if (valueEnd < 0) {
            valueEnd = src.indexOf(')', next + key.length());
          }

          if (valueEnd > 0) {
            String value = src.substring(next + key.length() + 1, valueEnd);
            sb.append(this.mask(value));
            start = valueEnd;
            continue;
          }

          String value = src.substring(next + key.length() + 1);
          sb.append(this.mask(value));
          break;
        }

        // JSON
        if (LogMask.isQuoteChar(leftChar) && LogMask.isQuoteChar(rightChar)) {
          int valueStart = src.indexOf(':', next + key.length());
          String keyQuote = src.substring(next + key.length(), valueStart);
          int valueEnd = src.indexOf(keyQuote + ",", valueStart + keyQuote.length() + 1);
          if (valueEnd < 0) {
            valueEnd = src.indexOf(keyQuote + "}", valueStart + keyQuote.length() + 1);
          }

          String value = src.substring(valueStart + keyQuote.length() + 1, valueEnd);
          sb.append(src, next + key.length(), valueStart + keyQuote.length() + 1);
          sb.append(this.mask(value));
          start = valueEnd;
          continue;
        }

        start = next + key.length();
      } while (true);

      return sb.toString();
    }

    public String maskResult(String s) {
      val maskLen = s.length() - leftKeep - rightKeep;
      if (s.length() < mask.length() || maskLen <= 0) {
        return mask;
      }

      val sb = new StringBuilder();

      if (leftKeep > 0) {
        sb.append(s, 0, leftKeep);
      }

      if (this.maskLength == -2) {
        sb.append(mask);
      } else if (this.maskLength == -1) {
        appendLength(maskLen, sb);
      } else if (this.maskLength >= 0) {
        appendLength(this.maskLength, sb);
      } else {
        sb.append(mask);
      }

      if (rightKeep > 0) {
        sb.append(s.substring(s.length() - rightKeep));
      }

      return sb.toString();
    }

    private void appendLength(int maskLength, StringBuilder sb) {
      for (int i = 0, j = maskLength / mask.length(); i < j; i++) {
        sb.append(mask);
      }

      int leftLength = maskLength % mask.length();
      if (leftLength > 0) {
        sb.append(mask, 0, leftLength);
      }
    }

    public void setKeep(String keep) {
      val parts = keep.split("\\.");
      if (parts.length == 1) {
        int v = Integer.parseInt(parts[0]);
        this.leftKeep = v;
        this.rightKeep = v;
      } else if (parts.length >= 2) {
        this.leftKeep = Integer.parseInt(parts[0]);
        this.rightKeep = Integer.parseInt(parts[1]);

        if (parts.length >= 3) {
          this.maskLength = Integer.parseInt(parts[2]);
        }
      }
    }
  }
}
