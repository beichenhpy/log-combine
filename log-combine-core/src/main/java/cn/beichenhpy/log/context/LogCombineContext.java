package cn.beichenhpy.log.context;

import cn.beichenhpy.log.entity.LogInfo;
import cn.beichenhpy.log.enums.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 日志上下文
 * CREATE_TIME: 2022/4/23 15:51
 *
 * @author beichenhpy
 * @version 1.0.0
 */
public class LogCombineContext {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private final ThreadLocal<LogInfo> LogLocalStorage = new InheritableThreadLocal<>();

    /**
     * 获取缩略包名
     *
     * @param className 源类名
     * @return 缩略后的类名
     */
    private static String getAbbreviateClassName(String className) {
        List<String> srcList = Arrays.asList(className.split("\\."));
        List<String> dstList = new LinkedList<>();
        srcList.stream()
                .limit(srcList.size() - 1L)
                .forEach(item -> dstList.add(item.substring(0, 1)));
        dstList.add(srcList.get(srcList.size() - 1));
        return String.join(".", dstList);
    }

    /**
     * 获取上下文 内部类懒加载
     *
     * @return 上下文
     */
    public static LogCombineContext getContext() {
        return LogCombineContextHolder.instance;
    }

    /**
     * 添加日志到上下文
     *
     * @param msg        消息
     * @param line       当前行
     * @param level      日志等级
     * @param className  当前类名
     * @param param      可变长参数
     * @param threadName log线程名
     */
    public void addLog(String msg, Integer line, LogLevel level, String className, String threadName, Object... param) {
        LocalDateTime now = LocalDateTime.now();
        String time = dateTimeFormatter.format(now);
        String logMsg = String.format("\n%s - [%s] %s %s - [%s] - %s", time, threadName, level, getAbbreviateClassName(className), line, msg);
        LogInfo logInfo = LogLocalStorage.get();
        if (logInfo == null) {
            logInfo = new LogInfo(logMsg, param);
        } else {
            logInfo.setFormat(logInfo.getFormat() + logMsg);
            Object[] originParams = logInfo.getParams();
            int newParamLength = param.length;
            int originParamsLength = originParams.length;
            int combineParamsLength = originParamsLength + newParamLength;
            //组合变量
            Object[] combineParams = new Object[combineParamsLength];
            System.arraycopy(originParams, 0, combineParams, 0, originParamsLength);
            System.arraycopy(param, 0, combineParams, originParamsLength, newParamLength);
            logInfo.setParams(combineParams);
        }
        LogLocalStorage.set(logInfo);
    }

    /**
     * 获取上下文的日志信息
     *
     * @return 返回日志信息
     */
    public LogInfo getLogLocalStorage() {
        return LogLocalStorage.get();
    }

    /**
     * 清除内容
     */
    public void clear() {
        LogLocalStorage.remove();
    }

    private static class LogCombineContextHolder {
        private static final LogCombineContext instance = new LogCombineContext();

        public LogCombineContextHolder() {

        }
    }
}
