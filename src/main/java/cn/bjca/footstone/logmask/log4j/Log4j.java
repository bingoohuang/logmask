package cn.bjca.footstone.logmask.log4j;

import lombok.experimental.UtilityClass;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

@UtilityClass
public class Log4j {
  public void config(String classpathConfigFile) {
    DOMConfigurator.configure(classpathResource(classpathConfigFile));
  }

  public URL classpathResource(String resourceName) {
    return Log4j.class.getClassLoader().getResource(resourceName);
  }
}
