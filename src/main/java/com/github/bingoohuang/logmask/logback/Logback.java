package com.github.bingoohuang.logmask.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import com.github.bingoohuang.logmask.impl.Clz;
import java.io.InputStream;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.slf4j.LoggerFactory;

/**
 * Logback指定XML配置文件
 *
 * @author bingoohuang
 */
@UtilityClass
public class Logback {
  /**
   * 通过指定类路径上的XML配置文件设置Logback的全局配置。
   *
   * @param classpathLogbackXmlFile 类路径上的XML配置文件名称。
   */
  @SneakyThrows
  public void config(String classpathLogbackXmlFile) {
    @Cleanup val fileStream = Clz.loadRes(classpathLogbackXmlFile);
    if (fileStream != null) {
      config(fileStream);
      return;
    }

    throw new RuntimeException(classpathLogbackXmlFile + " is not found");
  }

  @SneakyThrows
  public void config(InputStream configStream) {
    // assume SLF4J is bound to logback in the current environment
    val context = (LoggerContext) LoggerFactory.getILoggerFactory();

    val configurator = new JoranConfigurator();
    configurator.setContext(context);
    // Call context.reset() to clear any previous configuration, e.g. default
    // configuration. For multi-step configuration, omit calling context.reset().
    context.reset();
    configurator.doConfigure(configStream);

    StatusPrinter.printInCaseOfErrorsOrWarnings(context);
  }
}
