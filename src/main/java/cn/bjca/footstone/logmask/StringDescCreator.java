package cn.bjca.footstone.logmask;

import javassist.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class StringDescCreator {
  public static StringDesc createStringDesc(Class<?> clazz) {
    try {
      ClassPool pool = ClassPool.getDefault();
      pool.insertClassPath(new ClassClassPath(StringDesc.class));
      pool.insertClassPath(new ClassClassPath(clazz));
      CtClass stringDescCt = pool.get(StringDesc.class.getName());
      // beanStringDescCt是动态生成的类
      CtClass beanStringDescCt = pool.makeClass(clazz.getSimpleName() + "StringDesc");
      // 设置动态类的父类是StringDesc
      beanStringDescCt.setSuperclass(stringDescCt);
      // strBuilder用于构建toString方法体
      val strBuilder = new StringBuilder();
      String beanClassName = clazz.getName();
      strBuilder.append("java.lang.StringBuilder sb = new java.lang.StringBuilder();");
      // 输出bean类名
      strBuilder.append("sb.append(\"" + beanClassName + "(\");");
      CtClass beanCt = pool.get(beanClassName);
      CtField[] fields = beanCt.getDeclaredFields();
      boolean first = true;

      for (CtField field : fields) {
        if (!first) {
          strBuilder.append("sb.append(\", \");");
        }

        first = false;

        String fieldName = field.getName();
        try {
          val annotation = field.getAnnotation(Mask.class);
          val method = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
          if (annotation != null) {
            val d = (Mask) annotation;
            strBuilder.append(
                "sb.append(\""
                    + fieldName
                    + "=\" +mask((("
                    + beanClassName
                    + ")bean)."
                    + method
                    + "(), \""
                    + d.type()
                    + "\"));");
          } else {
            strBuilder.append(
                "sb.append(\""
                    + fieldName
                    + "=\" +(("
                    + beanClassName
                    + ")bean)."
                    + method
                    + "());");
          }

        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }

      strBuilder.append("sb.append(\")\");");
      strBuilder.append("return sb.toString();");
      // 动态构建toString方法
      CtMethod sm = new CtMethod(pool.get("java.lang.String"), "toString", null, beanStringDescCt);
      // 设置方法体
      String toString = strBuilder.toString();
      sm.setBody("{" + toString + "}");
      // 将toString方法添加到动态类中
      beanStringDescCt.addMethod(sm);
      // 生成动态类实例
      return (StringDesc) beanStringDescCt.toClass().newInstance();
    } catch (Exception e) {
      log.info("日志脱敏生成toString方法失败：", e);
    }

    return null;
  }
}
