package com.github.bingoohuang.logmask;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.logmask.impl.Clz;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LogMaskTest {
  @Test
  public void testMask() {
    LogMask.config(Clz.loadXML("logmask-test.xml", Config.class));
    assertEquals("a=1&password=___&b=2", LogMask.mask("a=1&password=123456&b=2"));

    Map<String, String> m = new HashMap<String, String>();
    m.put("a", "1");
    m.put("b", "2");
    m.put("password", "123456");
    assertEquals(
        "{'a':'1','b':'2','password':'___'}".replace('\'', '\"'),
        LogMask.mask(JSON.toJSONString(m)));
  }
}
