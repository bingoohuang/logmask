package cn.bjca.footstone.logmask;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class Slf4jMaskLayoutTest {
  @BeforeClass
  public static void beforeClass() {
    LogbackConfig.configFile("logback-mask.xml");
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

    val city = mapper.writeValueAsString(new Person.City("beijing", "beijing", 10));
    val addr = mapper.writeValueAsString(new Person.Addr(city, 100100));
    val bean = mapper.writeValueAsString(new Person.Bean("bingoohuang", 100, addr));
    log.info("bean：{}", bean);
  }
}
