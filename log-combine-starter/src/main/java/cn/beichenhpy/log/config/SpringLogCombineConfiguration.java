package cn.beichenhpy.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cn.beichenhpy.log.utils.LogCombineUtil.DEFAULT_PATTERN;

/**
 * 配置文件生成
 *
 * @author beichenhpy
 * @version 1.0.0
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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SpringLogCombineConfiguration)) {
            return false;
        } else {
            SpringLogCombineConfiguration other = (SpringLogCombineConfiguration) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$pattern = this.getPattern();
                Object other$pattern = other.getPattern();
                if (this$pattern == null) {
                    if (other$pattern != null) {
                        return false;
                    }
                } else if (!this$pattern.equals(other$pattern)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SpringLogCombineConfiguration;
    }

    public int hashCode() {
        int result = 1;
        Object $pattern = this.getPattern();
        result = result * 59 + ($pattern == null ? 43 : $pattern.hashCode());
        return result;
    }

    public String toString() {
        return "SpringLogCombineConfiguration(pattern=" + this.getPattern() + ")";
    }
}
