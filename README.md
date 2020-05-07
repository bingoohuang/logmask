# logmask

mask sensitive in the log

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
