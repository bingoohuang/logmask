package com.github.bingoohuang.logmask.json;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.impl.Clz;
import com.github.bingoohuang.logmask.logback.Logback;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class FastjsonReqTest {
  @BeforeClass
  public static void beforeClass() {
    Logback.config("logback-mask.xml");
  }

  @Test
  public void testFastJSON() {
    Req r =
        new Req("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");
    log.info(
        "testFastJSON params: {}",
        JSON.toJSONString(
            r, new MaskValueFilter(Clz.loadXML("logmask.xml", Config.class).setup())));
  }
}
