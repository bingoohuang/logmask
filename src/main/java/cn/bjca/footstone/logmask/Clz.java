package cn.bjca.footstone.logmask;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Clz {
  public static Map<String, String> loadValues(String classpath) {
    val map = new HashMap<String, String>(10);

    val regexTxt = Clz.loadResAsString(classpath);
    String lines[] = regexTxt.split("\\n");

    for (val line : lines) {
      String l = line.trim();
      if (l.length() == 0 || l.startsWith("#") || l.startsWith("//") || l.startsWith("--")) {
        continue;
      }

      int col = l.indexOf(":");
      if (col <= 0) {
        continue;
      }

      val name = l.substring(0, col).trim();
      if (name.length() == 0) {
        continue;
      }

      val value = l.substring(col + 1).trim();

      map.put(name, value);
    }

    return map;
  }

  @SneakyThrows
  public static String loadResAsString(String classpath) {
    @Cleanup val is = loadRes(classpath);
    return inputStreamToString(is);
  }

  /**
   * 从类路径加载资源文件。
   *
   * @param classpath 类路径
   * @return 输入流
   */
  public static InputStream loadRes(String classpath) {
    return Clz.class.getClassLoader().getResourceAsStream(classpath);
  }

  @SneakyThrows
  public static String inputStreamToString(InputStream is) {
    return bytesToString(inputStreamToByteArray(is));
  }

  public static String bytesToString(byte[] bytes) {
    return new String(bytes, Charset.forName("UTF-8"));
  }

  @SneakyThrows
  public static byte[] inputStreamToByteArray(InputStream is) {
    byte[] targetArray = new byte[is.available()];

    is.read(targetArray);

    return targetArray;
  }
}
