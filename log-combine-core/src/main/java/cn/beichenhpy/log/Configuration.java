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

package cn.beichenhpy.log;


import cn.beichenhpy.log.utils.LogCombineUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置文件
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    /**
     * 日志格式
     *
     * @see cn.beichenhpy.log.utils.LogCombineUtil#DEFAULT_PATTERN
     */
    private String pattern = LogCombineUtil.DEFAULT_PATTERN;

}
