package cn.bjca.footstone.logmask;

import cn.bjca.footstone.logmask.encrypt.Util;
import lombok.SneakyThrows;
import org.junit.Test;

public class DesTest {
  @Test
  @SneakyThrows
  public void des() {
    String k = Util.desKey();
    System.out.println("DES Key:" + k);

    String src = "五星红旗迎风飘扬胜利歌声多么响亮歌唱我们亲爱的祖国从今走向繁荣富强";
    String en = Util.des(src);
    System.out.println("Encrypted:" + en);
    String de = Util.desDecode(en);
    System.out.println("Decrypted:" + de);
  }
}
