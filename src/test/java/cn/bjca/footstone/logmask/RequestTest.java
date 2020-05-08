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
    LogbackConfig.configXMLFile("logback-mask.xml");
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
    @Mask(rule = "CARD")
    private String receiveCardNo;

    @Mask(rule = "MOBILE")
    private String mobNo;

    @Mask(rule = "EMAIL")
    private String email;

    @Mask(rule = "PASSWORD")
    private String payPasswd;
  }
}
