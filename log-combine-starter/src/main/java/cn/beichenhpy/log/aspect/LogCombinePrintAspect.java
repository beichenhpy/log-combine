/*
 * Copyright [2022] [han pengyu]
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

import cn.beichenhpy.log.context.LogCombineContext;
import cn.beichenhpy.log.context.LogCombineHelper;
import cn.beichenhpy.log.entity.LogInfo;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志合并打印切面
 * CREATE_TIME: 2022/4/23 16:28
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Aspect
public class LogCombinePrintAspect {


    private static final Logger logger = LoggerFactory.getLogger("combine-log have generated");


    @Pointcut(value = "@annotation(cn.beichenhpy.log.annotation.LogCombine)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void preLog() {
        LogCombineContext context = LogCombineContext.getContext();
        LogInfo localLogStorage = context.getLogLocalStorage();
        if (localLogStorage == null) {
            //init
            context.initContext("\n--------------------------Spring Aop AutoLog Called--------------------------");
        }
        //入嵌套
        context.pushNest();
    }

    @After(value = "pointCut()")
    public void logPrint() {
        LogCombineContext context = LogCombineContext.getContext();
        try {
            //出嵌套
            context.popNest();
        } catch (Throwable e) {
            logger.error("error:{},{}", e.getMessage(), e);
        } finally {
            if (context.getCurrentNest() == 0) {
                LogCombineHelper.print();
            }
        }
    }
}
