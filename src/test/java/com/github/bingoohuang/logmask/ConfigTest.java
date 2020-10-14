package com.github.bingoohuang.logmask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.bingoohuang.logmask.encrypt.Util;
import com.github.bingoohuang.logmask.impl.Clz;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

public class ConfigTest {
  @Test
  @SneakyThrows
  public void readXML() {
    val config = Clz.loadXML("logmask.xml", Config.class).setup();

    assertTrue(config.getMask().size() > 0);

    String s = "{\"creditcard\":\"12345678910\",\"ID\":\"1234\"}";
    String des = Util.des("12345678910", Util.K);
    assertEquals("{\"creditcard\":\"" + des + "\",\"ID\":\"***\"}", config.mask(s));

    String s1 =
        "username=1339162&password=Yinkman2015&custom%5Bimplementation%5D=commondefault&grant_type=password&scope=server";
    String s2 =
        "username=1339162&password=Yin***015&custom%5Bimplementation%5D=commondefault&grant_type=password&scope=server";
    String m = LogMask.mask(config, s1);
    assertEquals(s2, m);
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
