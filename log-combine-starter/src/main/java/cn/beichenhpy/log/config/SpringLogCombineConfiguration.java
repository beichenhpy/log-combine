package cn.beichenhpy.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.beichenhpy.log.utils.LogCombineUtil.DEFAULT_PATTERN;

/**
 * 配置文件生成
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@ConfigurationProperties(prefix = "log-combine")
public class SpringLogCombineConfiguration {

    private String pattern = DEFAULT_PATTERN;


    public SpringLogCombineConfiguration() {
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public String toString() {
        return "SpringLogCombineConfiguration(pattern=" + this.getPattern() + ")";
    }
}
