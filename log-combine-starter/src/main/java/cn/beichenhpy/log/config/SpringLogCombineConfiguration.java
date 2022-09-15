package cn.beichenhpy.log.config;

import cn.beichenhpy.log.utils.LogCombineInnerUtil;
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

    /**
     * @see LogCombineInnerUtil#DEFAULT_PATTERN
     */
    private String pattern = LogCombineInnerUtil.DEFAULT_PATTERN;
}
