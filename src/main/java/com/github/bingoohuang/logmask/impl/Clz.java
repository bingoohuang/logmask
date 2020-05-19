package com.github.bingoohuang.logmask.impl;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class Clz {
  public Map<String, String> loadValues(String classpath) {
    val map = new HashMap<String, String>(10);
    val regexTxt = Clz.loadResAsString(classpath);

    for (val line : regexTxt.split("\\n")) {
      String l = line.trim();
      if (l.length() == 0 || l.startsWith("#") || l.startsWith("//") || l.startsWith("--")) {
        continue;
      }

      int col = l.indexOf(':');
      if (col <= 0) {
        col = l.indexOf('=');
      }
      if (col > 0) {
        map.put(l.substring(0, col).trim(), l.substring(col + 1).trim());
      }
    }

    return map;
  }

  @SneakyThrows
  public String loadResAsString(String classpath) {
    @Cleanup val is = loadRes(classpath);
    return inputStreamToString(is);
  }

  /**
   * 从类路径加载资源文件。
   *
   * @param classpath 类路径
   * @return 输入流
   */
  public InputStream loadRes(String classpath) {
    return Clz.class.getClassLoader().getResourceAsStream(classpath);
  }

  public URL loadURL(String classpath) {
    return Clz.class.getClassLoader().getResource(classpath);
  }

  @SneakyThrows
  public String inputStreamToString(InputStream is) {
    return bytesToString(inputStreamToByteArray(is));
  }

  public String bytesToString(byte[] bytes) {
    return new String(bytes, Charset.forName("UTF-8"));
  }

  @SneakyThrows
  public byte[] inputStreamToByteArray(InputStream is) {
    byte[] targetArray = new byte[is.available()];

    int n = is.read(targetArray);

    return Arrays.copyOfRange(targetArray, 0, n);
  }

  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T> T loadXML(String classpath, Class<T> clazz) {
    val ju = JAXBContext.newInstance(clazz).createUnmarshaller();
    return (T) ju.unmarshal(new StringReader(Clz.loadResAsString(classpath)));
  }
}
