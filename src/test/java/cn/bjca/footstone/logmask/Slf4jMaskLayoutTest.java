package cn.bjca.footstone.logmask;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class Slf4jMaskLayoutTest {
    @BeforeClass
    public static void beforeClass() {
        LogbackConfig.configFile("logback-mask.xml");
    }

    @Test
    public void test() {
        log.info("我的18位身份证号码：12345612345678123X, 我的15位身份证号码：123456123456123");
        log.info("我的工作证号码：60476");
    }
}
