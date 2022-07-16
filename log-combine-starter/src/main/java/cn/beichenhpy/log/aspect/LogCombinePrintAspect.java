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

package cn.beichenhpy.log.aspect;

import cn.beichenhpy.log.LogCombineHelper;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志合并打印切面
 *
 * @author beichenhpy
 * @since 0.0.1
 */
@Aspect
public class LogCombinePrintAspect {

    private static final Logger log = LoggerFactory.getLogger(LogCombinePrintAspect.class);

    @Pointcut(value = "@annotation(cn.beichenhpy.log.annotation.LogCombine)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void preLog() {
        LogCombineHelper.pushNest();
    }

    @After(value = "pointCut()")
    public void logPrint() {
        try {
            LogCombineHelper.popNest();
        } catch (Throwable e) {
            log.error("error:{},{}", e.getMessage(), e);
        } finally {
            LogCombineHelper.print();
        }
    }
}
