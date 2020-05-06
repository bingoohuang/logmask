package cn.bjca.footstone.logmask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
  private String name;
  private String creditCard;
  private String id;

  @AllArgsConstructor
  public static class Bean {
    public String name;
    public int age;
    public String address;
  }

  @AllArgsConstructor
  public static class Addr {
    public String city;
    public int postcode;
  }

  @AllArgsConstructor
  public static class City {
    public String name;
    public String capital;
    public int zoneCode;
  }
}
