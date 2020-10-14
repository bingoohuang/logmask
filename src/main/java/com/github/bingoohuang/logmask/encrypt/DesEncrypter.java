package com.github.bingoohuang.logmask.encrypt;

import java.security.Key;

public class DesEncrypter extends Encrypter {
  private Key key = Util.K;

  @Override
  public void setup(String option) {
    if (!option.isEmpty()) {
      key = Util.parseKey(Util.DES_ALGORITHM, option);
    }
  }

  @Override
  public String encrypt(String src) {
    return Util.des(src, key);
  }
}
