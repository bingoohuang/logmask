package cn.bjca.footstone.logmask.fastjson;

import cn.bjca.footstone.logmask.Request;
import cn.bjca.footstone.logmask.logback.Config;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class RequestTest {
  @BeforeClass
  public static void beforeClass() {
    Config.configXMLFile("logback-mask.xml");
  }

  @Test
  public void testFastJSON() {
    Request r =
        new Request(
            "1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

    log.info("testFastJSON params: {}", JSON.toJSONString(r, new MaskValueFilter()));
  }
}
