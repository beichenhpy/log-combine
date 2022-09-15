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


import cn.beichenhpy.log.entity.ParsedPattern;
import cn.beichenhpy.log.utils.LogCombineInnerUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 配置文件
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    /**
     * 日志格式
     *
     * @see LogCombineInnerUtil#DEFAULT_PATTERN
     */
    private String pattern = LogCombineInnerUtil.DEFAULT_PATTERN;

    /**
     * 设置pattern 并重新初始化参数
     *
     * @param pattern 日志格式
     */
    protected void setPattern(String pattern) {
        this.pattern = pattern;
        ParsedPattern newParsedPattern = LogCombineContext.loadPattern();
        ParsedPattern parsedPattern = LogCombineContext.getParsedPattern();
        parsedPattern.setLogFormat(newParsedPattern.getLogFormat());
        parsedPattern.setKeyWords(newParsedPattern.getKeyWords());
        parsedPattern.setLoggerLengths(newParsedPattern.getLoggerLengths());
        parsedPattern.setDateTimeFormatters(newParsedPattern.getDateTimeFormatters());
    }
}
