package cn.bjca.footstone.logmask;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class LogbackConfig {
  @SneakyThrows
  public static void configFile(String classpathLogbackXmlFile) {
    @Cleanup val fileStream = classpathResource(classpathLogbackXmlFile);
    if (fileStream != null) {
      config(fileStream);
      return;
    }

    throw new RuntimeException(classpathLogbackXmlFile + " is not found");
  }

  @SneakyThrows
  public static void config(InputStream configStream) {
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

  public static InputStream classpathResource(String resourceName) {
    return LogbackConfig.class.getClassLoader().getResourceAsStream(resourceName);
  }
}
