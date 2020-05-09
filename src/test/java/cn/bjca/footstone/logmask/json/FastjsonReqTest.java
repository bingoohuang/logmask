package cn.bjca.footstone.logmask.json;

import cn.bjca.footstone.logmask.Rules;
import cn.bjca.footstone.logmask.logback.Logback;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class FastjsonReqTest {
  @BeforeClass
  public static void beforeClass() {
    Logback.configXMLFile("logback-mask.xml");
  }

  @Test
  public void testFastJSON() {
    Req r =
        new Req("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");
    log.info(
        "testFastJSON params: {}",
        JSON.toJSONString(r, new MaskValueFilter(Rules.load("logmask.rules"))));
  }
}
