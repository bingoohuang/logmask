package cn.bjca.footstone.logmask;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class MaskMeRequestTest {
  @BeforeClass
  public static void beforeClass() {
    LogbackConfig.configFile("logback-maskme.xml");
  }

  @Test
  public void testMaskMe() {
    val r =
        new MaskMeRequest("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678");
    log.info("MaskMeRequest params: {}", r);
  }
}
