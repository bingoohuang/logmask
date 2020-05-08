package cn.bjca.footstone.logmask;

import cn.bjca.footstone.logmask.logback.Config;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class ToStringTest {
  @BeforeClass
  public static void beforeClass() {
    Config.configXMLFile("logback-mask.xml");
  }

  Request r =
      new Request(
          "1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

  @Test
  public void testToString() {
    log.info("MaskMeRequest params: {}", r);
  }
}
