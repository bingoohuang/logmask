package com.github.bingoohuang.logmask;

import com.github.bingoohuang.logmask.encrypt.DesEncrypter;
import com.github.bingoohuang.logmask.encrypt.Encrypter;
import com.github.bingoohuang.logmask.encrypt.Util;
import com.github.bingoohuang.logmask.impl.ToString;
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
    if (obj == null) {
      return null;
    }

    if (obj.getClass().isAnnotationPresent(com.github.bingoohuang.logmask.Mask.class)) {
      val desc = ToString.create(obj.getClass());
      if (desc != null) {
        desc.setBean(obj);
        desc.setConf(this);

        return desc.toString();
      }
    }

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
    private List<String> keys;

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

    @XmlTransient private Encrypter encrypter;
    @XmlTransient private Pattern compiled;
    @XmlTransient private int leftKeep;
    @XmlTransient private int rightKeep;
    /** 掩码长度: -2：直接是掩码长度 -1: 原始长度 >= 0: 指定长度 */
    @XmlTransient private int maskLength = -2;

    public Mask setup(Map<String, Mask> rules) {
      if (encrypt != null) {
        setupEncrypt(encrypt);
      }
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

      for (val k : keys) {
        for (val key : k.split("\\s+")) {
          dest = keyMask(key, dest);
        }
      }
      return dest;
    }

    private String keyMask(String key, String src) {
      val upperKey = key.toUpperCase();
      val upperSrc = src.toUpperCase();
      int start = upperSrc.indexOf(upperKey);
      if (start < 0) {
        return src;
      }

      val sb = new StringBuilder();
      int keyLen = key.length();
      start = 0;

      do {
        int next = upperSrc.indexOf(upperKey, start);
        if (next < 0) {
          sb.append(src.substring(start));
          break;
        }

        sb.append(src, start, next + keyLen);

        char leftChar = ' ';
        char rightChar = ' ';
        if (next > 0) {
          leftChar = src.charAt(next - 1);
        }
        if (next + keyLen < src.length()) {
          rightChar = src.charAt(next + keyLen);
        }

        if (!LogMask.isBoundaryChar(leftChar, rightChar)) {
          start = next + keyLen;

          continue;
        }

        // key:value or key=value
        if (isToStringKey(leftChar, rightChar)) {
          start = processToString(keyLen, src, sb, next, rightChar);
          if (start < 0) break;
          continue;
        }

        // JSON
        if (isJsonKey(leftChar, rightChar)) {
          start = processJSON(key, src, sb, next);
          continue;
        }

        start = next + keyLen;
      } while (true);

      return sb.toString();
    }

    private int processJSON(String key, String src, StringBuilder sb, int next) {
      int valueStart = src.indexOf(':', next + key.length());
      String keyQuote = src.substring(next + key.length(), valueStart);
      int from = valueStart + keyQuote.length() + 1;
      int valueEnd = src.indexOf(keyQuote + ",", from);
      if (valueEnd < 0) {
        valueEnd = src.indexOf(keyQuote + "}", from);
      }

      String value = src.substring(from, valueEnd);
      sb.append(src, next + key.length(), from);
      sb.append(this.maskResult(value));
      return valueEnd;
    }

    private static final String endChars = "&, \t\r\n)]";

    private int processToString(int keyLen, String src, StringBuilder sb, int next, char rightCh) {
      sb.append(rightCh);

      int valueEnd = -1;

      int from = next + keyLen + 1;
      for (char c : endChars.toCharArray()) {
        valueEnd = src.indexOf(c, from);
        if (valueEnd >= 0) {
          break;
        }
      }

      String value = valueEnd >= 0 ? src.substring(from, valueEnd) : src.substring(from);

      sb.append(this.maskResult(value));

      return valueEnd;
    }

    private boolean isJsonKey(char leftChar, char rightChar) {
      return LogMask.isQuoteChar(leftChar) && LogMask.isQuoteChar(rightChar);
    }

    private boolean isToStringKey(char leftChar, char rightChar) {
      return LogMask.isBoundaryChar(leftChar) && (rightChar == ':' || rightChar == '=');
    }

    public String maskResult(String s) {
      if (s == null) {
        s = "";
      }

      if (this.encrypter != null) {
        return this.encrypter.encrypt(s);
      }

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

    private void setupEncrypt(String config) {
      String algorithm = config;

      int p = config.indexOf(':');
      String options = "";
      if (p > 0) {
        algorithm = config.substring(0, p);
        options = config.substring(p + 1);
      }

      if (algorithm.equalsIgnoreCase(Util.DES_ALGORITHM)) {
        this.encrypter = new DesEncrypter();
        this.encrypter.setup(options);
        return;
      }

      try {
        this.encrypter = (Encrypter) Class.forName(algorithm).getConstructor().newInstance();
        this.encrypter.setup(options);
      } catch (Exception ex) {
        log.warn("failed to initiate encrypter", ex);
      }
    }
  }
}
