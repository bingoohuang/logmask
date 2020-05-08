package cn.bjca.footstone.logmask.jackson;

import cn.bjca.footstone.logmask.logback.Config;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Slf4j
public class RequestTest {
  @BeforeClass
  public static void beforeClass() {
    Config.configXMLFile("logback-mask.xml");
  }

  Request r =
      new Request(
          "1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");

  @Ignore
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
}
