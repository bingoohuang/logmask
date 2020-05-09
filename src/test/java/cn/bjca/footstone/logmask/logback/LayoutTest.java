package cn.bjca.footstone.logmask.logback;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class LayoutTest {
  @BeforeClass
  public static void beforeClass() {
    Logback.configXMLFile("logback-layout.xml");
  }

  @Test
  @SneakyThrows
  public void test() {
    log.info("我的18位身份证号码：{}, 我的15位身份证号码：{}", "12345612345678123X", "123456123456123");
    log.info("我的工作证号码：{}", "60476");
    Person p = new Person("bingoo", "1112,\"222", "111");
    log.info("Person1：{}", p);

    val mapper = new ObjectMapper();

    log.info("Person2：{}", mapper.writeValueAsString(p));

    val city = mapper.writeValueAsString(new Person.City("bei\\\":\\\"jing", "beijing", 10));
    val addr = mapper.writeValueAsString(new Person.Addr(city, 100100));
    val bean = mapper.writeValueAsString(new Person.Bean("bingo\\\":\\\"ohuang", 100, addr));
    log.info("bean：{}", bean);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Person {
    private String name;
    private String creditCard;
    private String id;

    @AllArgsConstructor
    public static class Bean {
      public String name;
      public int age;
      public String address;
    }

    @AllArgsConstructor
    public static class Addr {
      public String city;
      public int postcode;
    }

    @AllArgsConstructor
    public static class City {
      public String name;
      public String capital;
      public int zoneCode;
    }
  }
}
