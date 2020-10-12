package com.github.bingoohuang.logmask.log4j2;

import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bingoohuang.logmask.logback.LayoutTest;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

@lombok.extern.log4j.Log4j2
public class Log4j2Test {
  @BeforeClass
  public static void beforeClass() {
    Log4j2.config("log4j2-mask.xml");
  }

  @Test
  @SneakyThrows
  public void hello() {
    log.info("我的18位身份证号码：{}, 我的15位身份证号码：{}", "12345612345678123X", "123456123456123");
    log.info("我的工作证号码：{}", "60476");
    LayoutTest.Person p = new LayoutTest.Person("bingoo", "1112,\"222", "111");
    log.info("Person1：{}", p);

    val mapper = new ObjectMapper();

    log.info("Person2：{}", mapper.writeValueAsString(p));

    val city =
        mapper.writeValueAsString(new LayoutTest.Person.City("bei\\\":\\\"jing", "beijing", 10));
    val addr = mapper.writeValueAsString(new LayoutTest.Person.Addr(city, 100100));
    val bean =
        mapper.writeValueAsString(new LayoutTest.Person.Bean("bingo\\\":\\\"ohuang", 100, addr));
    log.warn("bean：{}", bean);
    assertFalse(bean.isEmpty());
  }
}
