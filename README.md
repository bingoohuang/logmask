# logmask

[![Build Status](https://travis-ci.org/bingoohuang/logmask.svg?branch=master)](https://travis-ci.org/bingoohuang/logmask)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=com.github.bingoohuang%3Alogmask&metric=alert_status)](https://sonarcloud.io/dashboard/index/com.github.bingoohuang%3Alogmask)
[![Coverage Status](https://coveralls.io/repos/github/bingoohuang/logmask/badge.svg?branch=master)](https://coveralls.io/github/bingoohuang/logmask?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/logmask/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/logmask/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

mask sensitive in the log

## usage

### Config

logmask.xml配置文件

> 注：请放置于`classpath`，对应于maven工程结构的`src/main/resources`文件夹内   

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<config>
    <!-- rules定义 JSON序列化与toString序列化JavaBean时的规则 -->
    <rules>logmask.rules</rules>
    <mask>
        <!-- pattern 定义匹配的正则表达式 -->
        <pattern><![CDATA[\d{12}\d{3,5}[xX]?]]></pattern>
        <!-- boundary 定义pattern做正则匹配时，是否需要两边设定边界 -->
        <boundary>true</boundary>

        <!-- 指定json格式, key=value key:value形式的key，多个以空白分割 -->
        <keys>creditCard id name</keys>

        <!-- 脱敏后的数据保留, eg. -->
        <!-- 3: 首尾各保留3位原字符，例如abcdefg -> abc***efg -->
        <!-- 0,3: 尾部留3位原字符，例如abcdefg -> ***efg -->
        <!-- 3,0: 首部留3位原字符，例如abcdefg -> abc*** -->
        <keep>3</keep>

        <!-- mask定义脱敏后用于替换的掩码字符串 -->
        <mask>***</mask>

        <!-- keepMasksLength表示掩码长度是否与原始字符串长度一样长 -->
        <keepMasksLength>true</keepMasksLength>
    </mask>
    <mask>
        <pattern><![CDATA[\d{5}]]></pattern>
        <boundary>true</boundary>
    </mask>
</config>
```

logmask.rules规则配置文件:

> 注：请放置于`classpath`，对应于maven工程结构的`src/main/resources`文件夹内

```
# 姓名 匹配正则表达式与脱敏替换形式
NAME_REG=([\u4E00-\u9FA5]{1})[\u4E00-\u9FA5]{1,}
NAME_REPLACE=$1**

# 手机号 匹配正则表达式与脱敏替换形式
MOBILE_REG=(\d{3})\d{4}(\d{4})
MOBILE_REPLACE=$1****$2

# 邮箱 匹配正则表达式与脱敏替换形式
EMAIL_REG=(\w+)(@\w+)
EMAIL_REPLACE=******$2

# 卡号 匹配正则表达式
CARD_REG=(\w{4})(\w{4})(\w{4})(\w{4})
# 不定义默认的替换形式时，使用全局的掩码___
#CARD_REPLACE=$1*******$4
```

对应的JavaBean定义样例:

```java
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
```

### Direct API usage

directly use `LogMask.mask(r)` to generate masked string.

```java
@Slf4j
public class ToStringTest {
  @Test
  public void testToString() {
    Req r =
      new Req("1111222233334444", "18611112222", "bingoo.huang@gmail.com", "12345678", "beijing");
    log.info("request: {}", LogMask.mask(r));
  }
}
```

输出：

```log
2020-05-09 16:34:28.743 INFO  [main] cn.bjca.footstone.logmask.json.ToStringTest : request params: cn.bjca.footstone.logmask.json.Req(receiveCardNo=___, mobNo=186****2222, email=bingoo.******@gmail.com, address=beijing) 
```

## design

日志行范围|脱敏范围|配置示例|日志示例|API支持|实现状态
---     |---    | ---|---|-----|-----
日志行整行| 正则匹配| 形式1: <br>正则表达式 | | 1期实现
日志行整行| KEY锚定| 形式2: <br>keys=id creditCard address | `key=value`<br>`key='value'`<br>`key="value"`<br>`key=[value]`<br>`key=(value)`<br>`key={value}`<br><br>`key:value`<br>`key:'value'`<br>`key:"value"`<br>`key:[value]`<br>`key:(value)`<br>`key:{value}`|log4j/logback 自定义Layout| 1期实现 
日志行整行| 序号锚定| 形式2: <br>keys=#1 #3 separator=[]  | `[value1][value2][value3]`<br>`(value1)(value2)(value3)`<br>`{value1}{value3}{value2}`| | TODO
日志行整行| JSON KEY锚定|形式2: <br>keys=id creditCard address| `{"key":"value"}`|| 1期实现
日志行整行| XML TAG锚定 |形式2: <br>keys=id creditCard address| `<key>value</key>` || 1期实现
日志行中的信息块| 对象序列化toString/JSON/XML|  || `@LogMask(maskChars = "0")`<br>`private String creditCard;` | TODO
信息块中的子项| 直接API脱敏指定数据 | | | `LogMask.mask(creditCard)`;<br>`LogMask.mask(creditCard, MaskOption.maskChars("0"))`<br>`LogMask.maskBankCardNo(creditCard)`<br>`LogMask.maskMobileNumber(mobile)`<br>`LogMask.maskEmail(email)` | 1期实现

## resources

1. [Logback日志信息脱敏](https://medium.com/@lirenwork/logback%E6%97%A5%E5%BF%97%E4%BF%A1%E6%81%AF%E8%84%B1%E6%95%8F-99591ef57d43)
1. [MyBatis Type Handlers for Encrypt](https://github.com/drtrang/typehandlers-encrypt)
1. [java 日志脱敏框架 sensitive，优雅的打印脱敏日志](https://segmentfault.com/a/1190000017742745), [github houbb/sensitive](https://github.com/houbb/sensitive)
1. [google logback 日志脱敏](https://www.google.com/search?q=logback+%E6%97%A5%E5%BF%97%E8%84%B1%E6%95%8F)
1. [基于java反射，在运行时动态擦除对象中的敏感信息](https://github.com/Skydre/desensitization)
1. [日志脱敏 DestinyAries / log-tool](https://github.com/DestinyAries/log-tool)
1. [github log desensitization search](https://github.com/search?p=2&q=log+desensitization&type=Code)
1. [专治各种数据脱敏-Jackson-fastjson-logback](https://blog.csdn.net/qq_26418435/article/details/103620548)

## logback PatternLayout

[online](http://logback.qos.ch/manual/layouts.html)

| Format modifier | Left justify | Minimum width | Maximum width | Comment                                                                                                                                                           |
|-----------------|--------------|---------------|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| %20logger       | false        | 20            | none          | Left pad with spaces if the logger name is less than 20 characters long\.                                                                                         |
| %\-20logger     | true         | 20            | none          | Right pad with spaces if the logger name is less than 20 characters long\.                                                                                        |
| %\.30logger     | NA           | none          | 30            | Truncate from the beginning if the logger name is longer than 30 characters\.                                                                                     |
| %20\.30logger   | false        | 20            | 30            | Left pad with spaces if the logger name is shorter than 20 characters\. However, if logger name is longer than 30 characters, then truncate from the beginning\.  |
| %\-20\.30logger | true         | 20            | 30            | Right pad with spaces if the logger name is shorter than 20 characters\. However, if logger name is longer than 30 characters, then truncate from the beginning\. |
| %\.\-30logger   | NA           | none          | 30            | Truncate from the end if the logger name is longer than 30 characters\.                                                                                           |


Format modifier	|Logger name	|Result
-----------------|--------------|---------------
[%20.20logger]	|main.Name	| \[           main.Name\]
[%-20.20logger]	|main.Name	| \[main.Name           \]
[%10.10logger]	|main.foo.foo.bar.Name	|\[o.bar.Name\]
[%10.-10logger]	|main.foo.foo.bar.Name	|\[main.foo.f\]


