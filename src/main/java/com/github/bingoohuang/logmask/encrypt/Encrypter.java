package com.github.bingoohuang.logmask.encrypt;

public abstract class Encrypter {
  public void setup(String option) {}

  public abstract String encrypt(String src);
}
