package cn.bjca.footstone.logmask.log4j;

import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

public class Config {
  public static void configXMLFile(String classpathConfigFile) {
    DOMConfigurator.configure(classpathResource(classpathConfigFile));
  }

  public static URL classpathResource(String resourceName) {
    return Config.class.getClassLoader().getResource(resourceName);
  }
}
