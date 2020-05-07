package cn.bjca.footstone.logmask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MaskMe
public class MaskMeRequest {
  @MaskMe(type = "CARD")
  private String receiveCardNo;

  @MaskMe(type = "MOBILE")
  private String mobNo;

  @MaskMe(type = "EMAIL")
  private String email;

  @MaskMe(type = "PASSWORD")
  private String payPasswd;
}
