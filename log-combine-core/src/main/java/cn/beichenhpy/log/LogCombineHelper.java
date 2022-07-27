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
import cn.beichenhpy.log.enums.LogLevel;
import cn.beichenhpy.log.utils.LogCombineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class LogCombineHelper {

    private static final Logger log = LoggerFactory.getLogger(LogCombineHelper.class);
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
        ParsedPattern parsedPattern = LogCombineUtil.parsePattern(pattern);
        ParsedPattern applyParsedPattern = LogCombineContext.getParsedPattern();
        applyParsedPattern.setLogFormat(parsedPattern.getLogFormat());
        applyParsedPattern.setKeyWords(parsedPattern.getKeyWords());
        applyParsedPattern.setClassLengths(parsedPattern.getClassLengths());
        applyParsedPattern.setDateTimeFormatters(parsedPattern.getDateTimeFormatters());
    }

    /**
     * 获取调用本方法的行 <br>
     * 六层堆栈 apply(1) -> formatLog(2) -> addLog(3) -> getLineNumber(4) -> info/warn/...(5) -> 具体调用的方法(6)
     *
     * @return 行数
     */
    private static int getInvokeLineNumber() {
        return Thread.currentThread().getStackTrace()[6].getLineNumber();
    }


    /**
     * 获取调用本方法的行 <br>
     * 六层堆栈 apply(1) -> formatLog(2) -> addLog(3) -> getLineNumber(4) -> info/warn/...(5) -> 具体调用的方法(6)
     *
     * @return 行数
     */
    private static String getInvokeClassName() {
        return Thread.currentThread().getStackTrace()[6].getClassName();
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
        context.addLog(show -> show ? msg : "",
                show -> show ? getInvokeLineNumber() : "",
                show -> show ? LogLevel.TRACE : "",
                show -> show ? getInvokeClassName() : "",
                show -> show ? getThreadName() : "",
                param);
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
        context.addLog(show -> show ? msg : "",
                show -> show ? getInvokeLineNumber() : "",
                show -> show ? LogLevel.DEBUG : "",
                show -> show ? getInvokeClassName() : "",
                show -> show ? getThreadName() : "",
                param);
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
        context.addLog(show -> show ? msg : "",
                show -> show ? getInvokeLineNumber() : "",
                show -> show ? LogLevel.INFO : "",
                show -> show ? getInvokeClassName() : "",
                show -> show ? getThreadName() : "",
                param);
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
        context.addLog(show -> show ? msg : "",
                show -> show ? getInvokeLineNumber() : "",
                show -> show ? LogLevel.WARN : "",
                show -> show ? getInvokeClassName() : "",
                show -> show ? getThreadName() : "",
                param);
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
        context.addLog(show -> show ? msg : "",
                show -> show ? getInvokeLineNumber() : "",
                show -> show ? LogLevel.ERROR : "",
                show -> show ? getInvokeClassName() : "",
                show -> show ? getThreadName() : "",
                param);
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
}
