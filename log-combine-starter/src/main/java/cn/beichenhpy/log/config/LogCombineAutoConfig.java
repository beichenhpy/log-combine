package cn.beichenhpy.log.config;

import cn.beichenhpy.log.aspect.LogCombinePrintAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配类 注册切面bean
 * CREATE_TIME: 2022/4/23 16:27
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Configuration
public class LogCombineAutoConfig {


    @Bean
    public LogCombinePrintAspect logCombinePrintAspect() {
        return new LogCombinePrintAspect();
    }

}
