package cn.beichenhpy.log.config;

import cn.beichenhpy.log.LogCombineConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置文件生成
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@Data
@ConfigurationProperties(prefix = "log-combine")
public class SpringLogCombineConfiguration {

    private String pattern = LogCombineConstants.DEFAULT_PATTERN;
}
