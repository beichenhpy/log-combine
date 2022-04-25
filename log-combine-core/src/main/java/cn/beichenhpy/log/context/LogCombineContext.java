package cn.beichenhpy.log.context;

import cn.beichenhpy.log.entity.LogInfo;
import cn.beichenhpy.log.enums.LogLevel;
import org.slf4j.helpers.MessageFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Context for log combine
 * CREATE_TIME: 2022/4/23 15:51
 *
 * @author beichenhpy
 * @version 1.0.0
 */
public class LogCombineContext {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    private final ThreadLocal<LogInfo> LogLocalStorage = new InheritableThreadLocal<>();

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
        String logMsg = String.format("\n%s - [%s] %s %s - [%s] - %s", time, threadName, level, className, line, msg);
        String message = MessageFormatter.arrayFormat(logMsg, param).getMessage();
        LogInfo logInfo = LogLocalStorage.get();
        if (logInfo != null) {
            String originMessage = logInfo.getMessage();
            if (originMessage != null) {
                logInfo.setMessage(originMessage + message);
            }
        } else {
            logInfo = new LogInfo(message, 0);
        }
        setLogLocalStorage(logInfo);
    }

    /**
     * 设置日志信息到上下文
     *
     * @param logInfo 日志信息
     */
    public void setLogLocalStorage(LogInfo logInfo) {
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
