package cn.bjca.footstone.logmask.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Mask
public class Request {
  @Mask private String receiveCardNo;

  @Mask(rule = "MOBILE")
  private String mobNo;

  @Mask(rule = "EMAIL")
  private String email;

  @Mask(ignore = true)
  private String payPasswd;

  private String address;
}
