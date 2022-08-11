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

import cn.beichenhpy.log.enums.LogLevel;
import cn.beichenhpy.log.utils.LogCombineUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志合并打印工具类,使用方式
 * <pre>
 *     //在具体方法中使用
 *     public void test(){
 *         String a = "foo";
 *         LogCombineHelper.info("测试:{}", a);
 *         LogCombineHelper.warn("测试2:{}", a);
 *         //打印日志
 *         LogCombineHelper.print();
 *     }
 * </pre>
 *
 * @author beichenhpy
 * @since 0.0.1
 */
@Slf4j
public class LogCombineHelper {
    /**
     * 日志存储上下文
     */
    private static final LogCombineContext context = LogCombineContext.getContext();


    /**
     * 设置日志打印pattern
     *
     * @param pattern 格式
     */
    public static void setPattern(String pattern) {
        LogCombineContext.getConfiguration().setPattern(pattern);
    }


    /**
     * 添加TRACE
     *
     * @param msg   日志信息
     * @param param 参数
     */
    public static void trace(String msg, Object... param) {
        if (!log.isTraceEnabled()) {
            return;
        }
        context.addLog(msg, LogLevel.TRACE, param);
    }

    /**
     * 添加info
     *
     * @param msg   日志信息
     * @param param 参数
     */
    public static void debug(String msg, Object... param) {
        if (!log.isDebugEnabled()) {
            return;
        }
        context.addLog(msg, LogLevel.DEBUG, param);
    }


    /**
     * 添加info
     *
     * @param msg   日志信息
     * @param param 参数
     */
    public static void info(String msg, Object... param) {
        if (!log.isInfoEnabled()) {
            return;
        }
        context.addLog(msg, LogLevel.INFO, param);
    }

    /**
     * 添加WARN
     *
     * @param msg   日志信息
     * @param param 参数
     */
    public static void warn(String msg, Object... param) {
        if (!log.isWarnEnabled()) {
            return;
        }
        context.addLog(msg, LogLevel.WARN, param);
    }

    /**
     * 添加ERROR
     *
     * @param msg   日志信息
     * @param param 参数
     */
    public static void error(String msg, Object... param) {
        if (!log.isErrorEnabled()) {
            return;
        }
        context.addLog(msg, LogLevel.ERROR, param);
    }

    /**
     * 脱离一层嵌套
     */
    public static void popNest() {
        context.getLogLocalStorage().setNestedFloor(context.getLogLocalStorage().getNestedFloor() - 1);
    }

    /**
     * 进入一层嵌套
     */
    public static void pushNest() {
        if (context.getLogLocalStorage() == null) {
            //init
            context.initContext(null);
        }
        context.getLogLocalStorage().setNestedFloor(context.getLogLocalStorage().getNestedFloor() + 1);
    }

    /**
     * 获取当前嵌套层数
     *
     * @return -1 当前未初始化  否则返回层数
     */
    public static int getCurrentNest() {
        return context.getLogLocalStorage() == null ? -1 : context.getLogLocalStorage().getNestedFloor();
    }

    /**
     * 打印日志
     */
    public static void print() {
        if (getCurrentNest() == 0) {
            String logMsg = context.getLog(true);
            if (logMsg != null) {
                if (log.isTraceEnabled()) {
                    log.trace(LogCombineUtil.CURLY, logMsg);
                } else if (log.isDebugEnabled()) {
                    log.debug(LogCombineUtil.CURLY, logMsg);
                } else if (log.isInfoEnabled()) {
                    log.info(LogCombineUtil.CURLY, logMsg);
                } else if (log.isWarnEnabled()) {
                    log.warn(LogCombineUtil.CURLY, logMsg);
                } else if (log.isErrorEnabled()) {
                    log.error(LogCombineUtil.CURLY, logMsg);
                }
            }
        }
    }
}
