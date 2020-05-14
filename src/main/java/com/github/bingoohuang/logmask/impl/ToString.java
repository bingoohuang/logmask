package com.github.bingoohuang.logmask.impl;

import com.github.bingoohuang.logmask.Config;
import com.github.bingoohuang.logmask.Mask;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtField;
import javassist.CtMethod;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * 用于生成子类的StringDesc
 *
 * @author bingoohuang
 */
@Slf4j
@Data
public class ToString {
  protected Object bean;
  protected Config conf;

  public String mask(Object obj, String rule) {
    return conf.ruleMask(obj, rule);
  }

  public static ToString create(Class<?> clazz) {
    val classname = clazz.getSimpleName() + "ToString";

    try {
      ToString instance = newInstance(classname);
      if (instance != null) {
        return instance;
      }

      val pool = ClassPool.getDefault();

      pool.insertClassPath(new ClassClassPath(ToString.class));
      pool.insertClassPath(new ClassClassPath(clazz));
      // beanStringDescCt是动态生成的类
      val ctc = pool.makeClass(classname);
      // 设置动态类的父类是StringDesc
      ctc.setSuperclass(pool.get(ToString.class.getName()));
      // strBuilder用于构建toString方法体
      val sb = new StringBuilder();
      val bClass = clazz.getName();
      val sbClass = "java.lang.StringBuilder";
      sb.append(sbClass).append(" sb = new ").append(sbClass).append("();");
      // 输出bean类名
      sb.append("sb.append(\"").append(bClass).append("(\");");
      val beanCt = pool.get(bClass);
      boolean first = true;

      for (CtField field : beanCt.getDeclaredFields()) {
        val mask = (Mask) field.getAnnotation(Mask.class);
        if (mask != null && mask.empty()) {
          continue;
        }

        if (!first) {
          sb.append("sb.append(\", \");");
        }

        first = false;
        String fName = field.getName();

        sb.append("sb.append(\"")
            .append(fName)
            .append("=\" +mask(((")
            .append(bClass)
            .append(")bean).get")
            .append(capital(fName))
            .append("(), ");

        if (mask != null) {
          sb.append('"').append(mask.rule()).append("\"));");
        } else {
          sb.append("null));");
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
      val clz = ctc.toClass();
      return (ToString) clz.newInstance();
    } catch (Exception e) {
      log.info("日志脱敏生成toString方法失败：", e);
    }

    return null;
  }

  private static String capital(String fName) {
    return fName.substring(0, 1).toUpperCase() + fName.substring(1);
  }

  @SneakyThrows
  private static ToString newInstance(String className) {
    try {
      return (ToString) Class.forName(className).getConstructor().newInstance();
    } catch (ClassNotFoundException ignore) {
      // ignore
    }

    return null;
  }
}
