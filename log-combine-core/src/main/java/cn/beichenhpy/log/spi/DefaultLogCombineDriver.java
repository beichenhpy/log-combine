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
import cn.beichenhpy.log.utils.LogCombineUtil;

/**
 * <PRE>
 *
 * </PRE>
 * CREATE_TIME: 2022/6/18 22:08
 *
 * @author beichenhpy
 * @version 1.0.0
 */
public class DefaultLogCombineDriver implements LogCombineDriver {
    @Override
    public int order() {
        //默认的最先执行
        return Integer.MIN_VALUE;
    }

    @Override
    public Configuration initial() {
        Configuration defaultConfiguration = new Configuration();
        defaultConfiguration.setPattern(LogCombineUtil.DEFAULT_PATTERN);
        return defaultConfiguration;
    }
}
