package com.github.bingoohuang.logmask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mask注解，用于类动态生成toString方法使用。
 *
 * @author bingoohuang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Mask {
  /**
   * 预定义的规则名称.
   *
   * @return rule name
   */
  String rule() default "";

  /**
   * 是否直接忽略
   *
   * @return true/false
   */
  boolean ignore() default false;
}
