package cn.beichenhpy.log.spi;

import cn.beichenhpy.log.LogCombineContext;

/**
 * @author beichenhpy
 * @version 1.0.0
 */
public interface LogCombineDriver {

    /**
     * 默认的配置文件
     */
    Configuration DEFAULT_CONFIGURATION = LogCombineContext.getConfiguration();

    /**
     * 初始化配置文件
     *
     * @return 返回自定义的配置文件
     */
    Configuration initial();
}
