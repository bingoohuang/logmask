package com.github.bingoohuang.logmask.log4j2;

import org.junit.BeforeClass;
import org.junit.Test;

@lombok.extern.log4j.Log4j2
public class Log4j2Test {
  @BeforeClass
  public static void beforeClass() {
    Log4j2.config("log4j2-mask.xml");
  }

  @Test
  public void hell() {
    log.info("hello world.");
  }
}
