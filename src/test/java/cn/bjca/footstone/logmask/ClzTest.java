package cn.bjca.footstone.logmask;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.util.Map;

public class ClzTest {
  @Test
  @SneakyThrows
  public void Test1() {
    Map<String, String> map = Clz.loadValues("lines.txt");
    System.out.println(map);
    val mapper = new ObjectMapper();
    val city = mapper.writeValueAsString(new City("beijing", "beijing", 10));
    val addr = mapper.writeValueAsString(new Addr(city, 100100));
    val bean = mapper.writeValueAsString(new Bean("bingoohuang", 100, addr));

    System.out.println(bean);
  }

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
