package cn.bjca.footstone.logmask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class RequestTest {
  @BeforeClass
  public static void beforeClass() {
    LogbackConfig.configFile("logback-mask.xml");
  }

  @Test
  public void testMaskMe() {
    val r = new Request("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678");
    log.info("MaskMeRequest params: {}", r);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Mask
  public static class Request {
    @Mask(type = "CARD")
    private String receiveCardNo;

    @Mask(type = "MOBILE")
    private String mobNo;

    @Mask(type = "EMAIL")
    private String email;

    @Mask(type = "PASSWORD")
    private String payPasswd;
  }
}
