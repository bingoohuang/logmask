package com.github.bingoohuang.logmask.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.bingoohuang.logmask.jackson.Mask;
import com.github.bingoohuang.logmask.jackson.MaskSerializer;
import com.github.bingoohuang.logmask.logback.Logback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Slf4j
public class JacksonReqTest {
  @BeforeClass
  public static void beforeClass() {
    Logback.config("logback-mask.xml");
  }

  Request r =
      new Request(
          "1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

  @Ignore("Jackson字段脱敏注解实现有问题，待修复")
  @Test
  @SneakyThrows
  public void testJackson() {
    ObjectMapper mapper =
        JsonMapper.builder()
            .addModule(
                new SimpleModule("test", Version.unknownVersion())
                    .addSerializer(String.class, new MaskSerializer()))
            .build();
    log.info("testJackson params: {}", mapper.writeValueAsString(r));
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
