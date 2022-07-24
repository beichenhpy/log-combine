package cn.beichenhpy.log;

import cn.beichenhpy.log.config.SpringLogCombineConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 使用Spring的配置文件覆盖原始
 *
 * @author beichenhpy
 * @since 0.0.2
 */
public class InitialConfigurationRunner implements ApplicationRunner {

    @Resource
    private SpringLogCombineConfiguration logCombineConfiguration;

    @Override
    public void run(ApplicationArguments args) {
        if (StringUtils.hasText(logCombineConfiguration.getPattern())) {
            LogCombineHelper.setPattern(logCombineConfiguration.getPattern());
        }
    }
}
