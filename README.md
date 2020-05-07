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
