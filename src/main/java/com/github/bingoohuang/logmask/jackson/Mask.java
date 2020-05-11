package com.github.bingoohuang.logmask.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jackson序列化field注解.
 *
 * @author bingoohuang
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = MaskSerializer.class)
@JacksonAnnotationsInside
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
