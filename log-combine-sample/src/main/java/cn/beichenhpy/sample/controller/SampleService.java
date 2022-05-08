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

package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.annotation.LogCombine;
import cn.beichenhpy.log.context.LogCombineHelper;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *
 * </pre>
 *
 * @author beichenhpy
 * <p> 2022/4/24 09:07
 */
@Service
public class SampleService {

    @LogCombine
    public void test2() {
        LogCombineHelper.info("service:{}", 2);
    }


    public void test3() {
        LogCombineHelper.info("test3:{}", "test333");
        LogCombineHelper.print();
    }
}
