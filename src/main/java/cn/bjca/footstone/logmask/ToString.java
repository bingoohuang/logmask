package cn.bjca.footstone.logmask;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtField;
import javassist.CtMethod;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Map;

/**
 * 用于生成子类的StringDesc
 *
 * @author bingoobjca
 */
@Slf4j
@Data
public class ToString {
  private Map<String, RuleConfig> rulesMap;
  protected Object bean;

  public String mask(Object obj, String rule) {
    if (obj == null) {
      return null;
    }

    if (rule == null) {
      return obj.toString();
    }

    if (rulesMap != null && rulesMap.get(rule) != null) {
      return rulesMap.get(rule).mask(obj.toString());
    }

    return "___";
  }

  public static ToString create(Class<?> clazz) {
    try {
      val pool = ClassPool.getDefault();
      pool.insertClassPath(new ClassClassPath(ToString.class));
      pool.insertClassPath(new ClassClassPath(clazz));
      // beanStringDescCt是动态生成的类
      val ctc = pool.makeClass(clazz.getSimpleName() + "ToString");
      // 设置动态类的父类是StringDesc
      ctc.setSuperclass(pool.get(ToString.class.getName()));
      // strBuilder用于构建toString方法体
      val sb = new StringBuilder();
      val bClass = clazz.getName();
      val sbClass = "java.lang.StringBuilder";
      sb.append(sbClass + " sb = new " + sbClass + "();");
      // 输出bean类名
      sb.append("sb.append(\"" + bClass + "(\");");
      val beanCt = pool.get(bClass);
      boolean first = true;

      for (CtField field : beanCt.getDeclaredFields()) {
        val mask = (Mask) field.getAnnotation(Mask.class);
        if (mask != null && mask.ignore()) {
          continue;
        }

        if (!first) {
          sb.append("sb.append(\", \");");
        }

        first = false;
        String fName = field.getName();

        try {
          val getter = "get" + capital(fName);
          sb.append("sb.append(\"" + fName + "=\" +mask(((" + bClass + ")bean)." + getter + "(), ");

          if (mask != null) {
            sb.append("\"" + mask.rule() + "\"));");
          } else {
            sb.append("null));");
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      sb.append("sb.append(\")\");return sb.toString();");
      // 动态构建toString方法
      val sm = new CtMethod(pool.get("java.lang.String"), "toString", null, ctc);
      // 设置方法体
      sm.setBody("{" + sb.toString() + "}");
      // 将toString方法添加到动态类中
      ctc.addMethod(sm);
      // 生成动态类实例
      return (ToString) ctc.toClass().newInstance();
    } catch (Exception e) {
      log.info("日志脱敏生成toString方法失败：", e);
    }

    return null;
  }

  private static String capital(String fName) {
    return fName.substring(0, 1).toUpperCase() + fName.substring(1);
  }
}
