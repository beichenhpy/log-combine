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


import cn.beichenhpy.log.parser.ParserHelper;
import cn.beichenhpy.log.parser.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
     */
    private String pattern = LogCombineConstants.DEFAULT_PATTERN;


    /**
     * 设置pattern 并重新初始化参数
     *
     * @param pattern 日志格式
     */
    protected synchronized void setPattern(String pattern) {
        if (pattern != null) {
            this.pattern = pattern;
        }
        List<Pattern> parsedPatternList = LogCombineContext.getParsedPatternList();
        parsedPatternList.clear();
        ParserHelper helper = new ParserHelper();
        List<Pattern> parse = helper.parse(this.pattern);
        parsedPatternList.addAll(parse);
    }
}
