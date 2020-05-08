package cn.bjca.footstone.logmask.log4j;

import lombok.extern.log4j.Log4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Log4j
public class LayoutTest {
  @BeforeClass
  public static void beforeClass() {
    Config.configXMLFile("log4j-mask.xml");
  }

  @Test
  public void test() {
    log.info("我的18位身份证号码：12345612345678123X, 我的15位身份证号码：123456123456123");
    log.info("我的工作证号码：60476");
  }
}
