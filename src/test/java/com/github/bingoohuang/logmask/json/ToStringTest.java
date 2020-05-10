package com.github.bingoohuang.logmask.json;

import com.github.bingoohuang.logmask.LogMask;
import com.github.bingoohuang.logmask.logback.Logback;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class ToStringTest {
  @BeforeClass
  public static void beforeClass() {
    Logback.config("logback-mask.xml");
  }

  Req r =
      new Req("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

  @Test
  public void toStr() {
    log.info("MaskMeRequest1 params: {}", r);
    log.info("MaskMeRequest2 params: {}, null: {}", r, null);
    log.info("MaskMeRequest3 params: {}", LogMask.mask(r));
  }
}
