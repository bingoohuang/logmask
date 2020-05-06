package cn.bjca.footstone.logmask;

import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

public class Log4jConfig {
  public static void configXMLFile(String classpathConfigFile) {
    DOMConfigurator.configure(classpathResource(classpathConfigFile));
  }

  public static URL classpathResource(String resourceName) {
    return Log4jConfig.class.getClassLoader().getResource(resourceName);
  }
}
