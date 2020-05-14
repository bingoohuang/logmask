package com.github.bingoohuang.logmask.encrypt;

import java.security.Key;

public class DesEncrypter extends Encrypter {
  private Key key;

  @Override
  public void setup(String option) {
    if (option.isEmpty()) {
      key = Util.K;
    } else {
      key = Util.parseKey(Util.DES_ALGORITHM, option);
    }
  }

  @Override
  public String encrypt(String src) {
    return Util.des(src, key);
  }
}
