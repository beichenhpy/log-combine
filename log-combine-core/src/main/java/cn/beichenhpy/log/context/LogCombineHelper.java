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

package cn.beichenhpy.log.context;

import cn.beichenhpy.log.enums.LogLevel;
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
 * CREATE_TIME: 2022/4/23 16:12
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Slf4j
public class LogCombineHelper {

    /**
     * 日志存储上下文
     */
    private static final LogCombineContext context = LogCombineContext.getContext();


    /**
     * 获取调用本方法的行 <br>
     * 三层堆栈 getLineNumber(1) -> info/warn/...(2) -> 具体调用的方法(3)
     *
     * @return 行数
     */
    private static int getInvokeLineNumber() {
        return Thread.currentThread().getStackTrace()[3].getLineNumber();
    }


    /**
     * 获取调用本方法的行 <br>
     * 三层堆栈 getLineNumber(1) -> info/warn/...(2) -> 具体调用的方法(3)
     *
     * @return 行数
     */
    private static String getInvokeClassName() {
        return Thread.currentThread().getStackTrace()[3].getClassName();
    }

    /**
     * 获取当前线程名
     *
     * @return 线程名
     */
    private static String getThreadName() {
        return Thread.currentThread().getName();
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
        context.addLog(msg, getInvokeLineNumber(), LogLevel.TRACE, getInvokeClassName(), getThreadName(), param);
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
        context.addLog(msg, getInvokeLineNumber(), LogLevel.DEBUG, getInvokeClassName(), getThreadName(), param);
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
        context.addLog(msg, getInvokeLineNumber(), LogLevel.INFO, getInvokeClassName(), getThreadName(), param);
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
        context.addLog(msg, getInvokeLineNumber(), LogLevel.WARN, getInvokeClassName(), getThreadName(), param);
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
        context.addLog(msg, getInvokeLineNumber(), LogLevel.ERROR, getInvokeClassName(), getThreadName(), param);
    }


    /**
     * 打印日志
     */
    public static void print() {
        String logMsg = context.getLog(true);
        if (logMsg != null) {
            if (log.isTraceEnabled()) {
                log.trace("{}", logMsg);
            } else if (log.isDebugEnabled()) {
                log.debug("{}", logMsg);
            } else if (log.isInfoEnabled()) {
                log.info("{}", logMsg);
            } else if (log.isWarnEnabled()) {
                log.warn("{}", logMsg);
            } else if (log.isErrorEnabled()) {
                log.error("{}", logMsg);
            }
        }
    }
}
