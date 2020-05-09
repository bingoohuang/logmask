package cn.bjca.footstone.logmask.impl;

import lombok.val;

import java.util.Map;

public class Str {
  public static String stripPostfix(String s, String postfix) {
    return s.substring(0, s.length() - postfix.length());
  }

  public static String getOrDefault(Map<String, String> map, String key, String defaultValue) {
    val value = map.get(key);
    return value != null ? value : defaultValue;
  }
}
