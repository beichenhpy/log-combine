package cn.beichenhpy.log.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.beichenhpy.log.utils.LogCombineUtil.DEFAULT_PATTERN;

/**
 * 配置文件生成
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@Data
@ConfigurationProperties(prefix = "log-combine")
public class SpringLogCombineConfiguration {

    private String pattern = DEFAULT_PATTERN;
}
