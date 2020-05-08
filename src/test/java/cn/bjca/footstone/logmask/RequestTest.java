package cn.bjca.footstone.logmask;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class RequestTest {
  @BeforeClass
  public static void beforeClass() {
    LogbackConfig.configXMLFile("logback-mask.xml");
  }

  Request r =
      new Request(
          "1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

  @Test
  public void testMaskMe() {
    log.info("MaskMeRequest params: {}", r);
  }

  @Test
  public void testFastJSON() {
    log.info("testFastJSON params: {}", JSON.toJSONString(r, new MaskValueFilter()));
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Mask
  public static class Request {
    @Mask private String receiveCardNo;

    @Mask(rule = "MOBILE")
    private String mobNo;

    @Mask(rule = "EMAIL")
    private String email;

    @Mask(ignore = true)
    private String payPasswd;

    private String address;
  }
}
