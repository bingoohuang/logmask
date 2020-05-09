package cn.bjca.footstone.logmask.json;

import cn.bjca.footstone.logmask.Mask;
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

  @Mask(ignore = true)
  private String payPasswd;

  private String address;
}
