package cn.beichenhpy.log.annotation;

import cn.beichenhpy.log.config.LogCombineAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用日志合并打印功能，在Spring入口类添加
 * CREATE_TIME: 2022/4/23 16:24
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(LogCombineAutoConfig.class)
public @interface EnableLogCombine {
}
