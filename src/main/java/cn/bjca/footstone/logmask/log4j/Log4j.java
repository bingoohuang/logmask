package cn.bjca.footstone.logmask.log4j;

import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

public class Log4j {
  public static void configXMLFile(String classpathConfigFile) {
    DOMConfigurator.configure(classpathResource(classpathConfigFile));
  }

  public static URL classpathResource(String resourceName) {
    return Log4j.class.getClassLoader().getResource(resourceName);
  }
}
