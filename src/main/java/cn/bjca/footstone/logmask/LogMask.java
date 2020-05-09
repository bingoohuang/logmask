package cn.bjca.footstone.logmask;

import lombok.val;

public class LogMask {
  public static final String DEFAULT_MASK = "___";

  public static String mask(Object obj) {
    return mask(Clz.loadXML("logmask.xml", Config.class).setup(), obj);
  }

  public static String mask(Config config, Object obj) {
    if (obj == null) {
      return null;
    }

    if (obj.getClass().isAnnotationPresent(Mask.class)) {
      val desc = ToString.create(obj.getClass());
      if (desc != null) {
        desc.setBean(obj);
        desc.setConf(config);

        return desc.toString();
      }
    }

    return mask(config, obj.toString());
  }

  public static String mask(Config config, String src) {
    return keysMask(config, patternMask(config, src));
  }

  private static String keysMask(Config config, String src) {
    String dest = src;
    for (val p : config.getMask()) {
      if (p.getKeys() == null) {
        continue;
      }

      String[] keys = p.getKeys().split("\\s+");

      for (val key : keys) {
        dest = keyMask(p, key, dest);
      }
    }

    return dest;
  }

  private static String keyMask(Config.Mask mask, String key, String src) {
    int start = src.indexOf(key);
    if (start < 0) {
      return src;
    }

    val sb = new StringBuilder();
    start = 0;

    while (true) {
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

      if (!isBoundaryChar(leftChar, rightChar)) {
        start = next + key.length();

        continue;
      }

      // key:value or key=value
      if (isBlankChar(leftChar) && (rightChar == ':' || rightChar == '=')) {
        sb.append(rightChar);

        int valueEnd = src.indexOf(", ", next + key.length());
        if (valueEnd < 0) {
          valueEnd = src.indexOf(")", next + key.length());
        }

        if (valueEnd > 0) {
          String value = src.substring(next + key.length() + 1, valueEnd);
          sb.append(mask.replace(value));
          start = valueEnd;
          continue;
        }

        String value = src.substring(next + key.length() + 1);
        sb.append(mask.replace(value));
        break;
      }

      // JSON
      if (isQuoteChar(leftChar) && isQuoteChar(rightChar)) {
        int valueStart = src.indexOf(":", next + key.length());
        String keyQuote = src.substring(next + key.length(), valueStart);
        int valueEnd = src.indexOf(keyQuote + ",", valueStart + keyQuote.length() + 1);
        if (valueEnd < 0) {
          valueEnd = src.indexOf(keyQuote + "}", valueStart + keyQuote.length() + 1);
        }

        String value = src.substring(valueStart + keyQuote.length() + 1, valueEnd);
        sb.append(src, next + key.length(), valueStart + keyQuote.length() + 1);
        sb.append(mask.replace(value));
        start = valueEnd;
        continue;
      }

      start = next + key.length();
    }

    return sb.toString();
  }

  private static String patternMask(Config config, String src) {
    String dest = src;

    for (val p : config.getMask()) {
      if (p.getCompiled() == null) {
        continue;
      }

      int start = 0;
      val m = p.getCompiled().matcher(dest);
      val sb = new StringBuilder();

      while (m.find()) {
        if (m.start() > 0) {
          sb.append(src, start, m.start());
        }

        sb.append(p.replace(m.group()));
        start = m.end();
      }

      if (start > 0) {
        if (start < src.length()) {
          sb.append(src.substring(start));
        }

        dest = sb.toString();
      }
    }
    return dest;
  }

  private static boolean isQuoteChar(char c) {
    return c == '"' || c == '\\';
  }

  private static boolean isBoundaryChar(char l, char r) {
    return isBoundaryChar(l) && isBoundaryChar(r);
  }

  private static boolean isBlankChar(char c) {
    return c == ' ' || c == '\t';
  }

  private static boolean isBoundaryChar(char c) {
    return !(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9');
  }
}
