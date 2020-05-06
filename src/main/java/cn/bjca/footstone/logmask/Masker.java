package cn.bjca.footstone.logmask;

import lombok.val;

import java.util.List;

public class Masker {
  public static String mask(List<Mask> masks, String src) {
    for (val p : masks) {
      int start = 0;
      val m = p.getCompiled().matcher(src);
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

        src = sb.toString();
      }
    }

    return src;
  }
}
