package cn.bjca.footstone.logmask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mask注解，用于类动态生成toString方法使用。
 *
 * @author bingoobjca
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Mask {
  String rule() default "";
}
