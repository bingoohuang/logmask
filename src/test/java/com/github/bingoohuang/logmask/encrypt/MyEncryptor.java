package com.github.bingoohuang.logmask.encrypt;

public class MyEncryptor extends Encrypter {
  private String prefix;

  @Override
  public void setup(String option) {
    this.prefix = option;
  }

  @Override
  public String encrypt(String src) {
    return prefix + src;
  }
}
