package com.github.bingoohuang.logmask;

import com.github.bingoohuang.logmask.impl.Clz;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
  @Test
  @SneakyThrows
  public void readXML() {
    val config = Clz.loadXML("logmask.xml", Config.class);

    assertTrue(config.getMask().size() > 0);
  }

  @Test
  public void mask() {
    Config.Mask mask = new Config.Mask();
    mask.setKeep("3.3.-1");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("hel##########ang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("hel###ang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3.-2");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("hel###ang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3.0");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("helang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3.1");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("hel#ang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3.4");
    mask.setMask("###");
    assertEquals("hel####ang", mask.maskResult("hellobingoohuang"));

    mask = new Config.Mask();
    mask.setKeep("3.3.11");
    mask.setMask("###");
    mask.setup(null);
    assertEquals("hel###########ang", mask.maskResult("hellobingoohuang"));
  }
}
