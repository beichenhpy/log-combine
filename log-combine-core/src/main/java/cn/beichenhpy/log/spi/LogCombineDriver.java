/*
 * Copyright 2022 韩鹏宇
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package cn.beichenhpy.log.spi;

import cn.beichenhpy.log.Configuration;

/**
 * SPI实现接口，用于实现配置文件 <br/>
 *
 * @author beichenhpy
 * @see DefaultLogCombineDriver 参考默认驱动
 * @since 0.0.2
 */
public interface LogCombineDriver {


    /**
     * 执行顺序
     *
     * @return 选择执行顺序
     */
    int order();

    /**
     * 初始化配置文件
     *
     * @return 返回自定义的配置文件
     */
    Configuration initial();
}
