package cn.beichenhpy.log.config;

import cn.beichenhpy.log.aspect.LogCombinePrintAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Config for aspect and ThreadPool
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


    @Bean(name = "logThreadPoolExecutor")
    public Executor logThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(40);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(300);
        executor.setThreadNamePrefix("log-combine-");
        // use caller Thread to run
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
