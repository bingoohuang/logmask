package cn.bjca.footstone.logmask;

import cn.bjca.footstone.logmask.impl.Clz;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConfigTest {
  @Test
  @SneakyThrows
  public void readXML() {
    val config = Clz.loadXML("logmask.xml", Config.class);

    assertTrue(config.getMask().size() > 0);
  }
}
