package cn.bjca.footstone.logmask.encrypt;

import lombok.SneakyThrows;
import lombok.val;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Util {
  public static final String baseKey = "FUojtYy6ugs";

  public static String des(String src) {
    return des(src, baseKey);
  }

  public static String desDecode(String src) {
    return desDecode(src, baseKey);
  }

  @SneakyThrows
  public static String des(String src, String key) {
    val c = Cipher.getInstance("DES");
    c.init(Cipher.ENCRYPT_MODE, parseKey("DES", key));

    return base64(c.doFinal(src.getBytes("UTF8")));
  }

  @SneakyThrows
  public static String desDecode(String src, String key) {
    val c = Cipher.getInstance("DES");
    c.init(Cipher.DECRYPT_MODE, parseKey("DES", key));

    return new String(c.doFinal(base64Decode(src)), "UTF8");
  }

  @SneakyThrows
  public static String desKey() {
    return Util.generateKey("DES");
  }

  @SneakyThrows
  public static String generateKey(String algorithm) {
    return base64(KeyGenerator.getInstance(algorithm).generateKey().getEncoded());
  }

  @SneakyThrows
  public static SecretKey parseKey(String algorithm, String key) {
    return new SecretKeySpec(base64Decode(key), algorithm);
  }

  public static String base64(byte[] bytes) {
    String s = DatatypeConverter.printBase64Binary(bytes).split("=")[0];
    s = s.replace('+', '-');
    return s.replace('/', '_');
  }

  public static byte[] base64Decode(String s) {
    int l = s.length() % 4;
    StringBuilder sb = new StringBuilder(s);
    for (int i = 0; l > 0 && i < 4 - l; i++) {
      sb.append('=');
    }

    String src = sb.toString().replace('-', '+');
    src = src.replace('_', '/');
    return DatatypeConverter.parseBase64Binary(src);
  }
}
