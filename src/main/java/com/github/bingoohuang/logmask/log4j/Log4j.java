package com.github.bingoohuang.logmask.log4j;

import com.github.bingoohuang.logmask.impl.Clz;
import lombok.experimental.UtilityClass;
import org.apache.log4j.xml.DOMConfigurator;

@UtilityClass
public class Log4j {
  public void config(String classpathConfigFile) {
    DOMConfigurator.configure(Clz.loadURL(classpathConfigFile));
  }
}
