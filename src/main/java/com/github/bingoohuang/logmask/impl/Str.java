package com.github.bingoohuang.logmask.impl;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Map;

@UtilityClass
public class Str {
  public String stripPostfix(String s, String postfix) {
    return s.substring(0, s.length() - postfix.length());
  }

  public String getOrDefault(Map<String, String> map, String key, String defaultValue) {
    val value = map.get(key);
    return value != null ? value : defaultValue;
  }
}
