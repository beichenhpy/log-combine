package cn.beichenhpy.log;

import cn.beichenhpy.log.config.SpringLogCombineConfiguration;
import cn.beichenhpy.log.spi.Configuration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;

/**
 * 使用Spring的配置文件覆盖原始
 *
 * @author beichenhpy
 * @version 1.0.0
 */
public class InitialConfigurationRunner implements ApplicationRunner {

    @Resource
    private SpringLogCombineConfiguration logCombineConfiguration;

    @Override
    public void run(ApplicationArguments args) {
        Configuration configuration = new Configuration(
                logCombineConfiguration.getPattern()
        );
        LogCombineContext.setConfiguration(configuration);
    }
}
