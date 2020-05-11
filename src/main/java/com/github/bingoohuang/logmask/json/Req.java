package com.github.bingoohuang.logmask.json;

import com.github.bingoohuang.logmask.Mask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Mask
public class Req {
  @Mask private String receiveCardNo;

  @Mask(rule = "MOBILE")
  private String mobNo;

  @Mask(rule = "EMAIL")
  private String email;

  @Mask(empty = true)
  private String payPasswd;

  private String address;
}
