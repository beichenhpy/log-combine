package cn.beichenhpy.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 所有添加了该注解的方法，并使用{@code LogCombineHelper.xxx}的，都会合并日志打印
 * CREATE_TIME: 2022/4/23 16:32
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogCombine {
}
