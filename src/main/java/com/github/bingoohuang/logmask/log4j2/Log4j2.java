package com.github.bingoohuang.logmask.log4j2;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.core.config.Configurator;

@UtilityClass
public class Log4j2 {
  @SneakyThrows
  public void config(String classpathConfigFile) {
    // https://stackoverflow.com/a/49334573
    Configurator.initialize(null, "classpath:" + classpathConfigFile);
  }
}
