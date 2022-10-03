package cn.beichenhpy.log.parser;

import cn.beichenhpy.log.enums.LogLevel;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:19
 */
public class ParseUtil {

    //public
    public static final String DEFAULT_PATTERN = "%date{yyyy-MM-dd HH:mm:ss,SSS}  %level %pid --- [%thread]  %logger{35} - [%line] :%msg";
    public static final String LOG_KEY_WORD_PID = "pid";
    public static final String LOG_KEY_WORD_THREAD = "thread";
    public static final String LOG_KEY_WORD_LEVEL = "level";
    public static final String LOG_KEY_WORD_LINE = "line";
    public static final String LOG_KEY_WORD_MSG = "msg";
    public static final String LOG_KEY_WORD_DATE = "date";
    public static final String LOG_KEY_WORD_LOGGER = "logger";
    public static final String LOG_KEY_WORD_LINE_SEPARATOR = "n";
    public static final String DEFAULT_DATETIME_FORMATTER_STR = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_CACHE = new HashMap<>(8);
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMATTER_STR);
    public static final int DEFAULT_LOGGER_LENGTH = 35;

    static {
        DATE_TIME_FORMATTER_CACHE.put(DEFAULT_DATETIME_FORMATTER_STR, DEFAULT_DATETIME_FORMATTER);
    }


    public static DateTimeFormatter getDateTimeFormatter(String pattern) {
        if (pattern == null) {
            return DEFAULT_DATETIME_FORMATTER;
        }
        DateTimeFormatter dateTimeFormatter = DATE_TIME_FORMATTER_CACHE.get(pattern);
        if (dateTimeFormatter == null) {
            try {
                dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            } catch (IllegalArgumentException e) {
                System.out.println("[LOG-COMBINE]: 您输入的日期格式化格式: [" + pattern + "]有误，无法找到对应的处理器。");
                throw e;
            }
            DATE_TIME_FORMATTER_CACHE.put(pattern, dateTimeFormatter);
        }
        return dateTimeFormatter;
    }


    public static int getLoggerLength(String length) {
        if (length == null) {
            return DEFAULT_LOGGER_LENGTH;
        }
        try {
            return Integer.parseInt(length);
        } catch (NumberFormatException e) {
            System.out.println("[LOG-COMBINE]: 您输入的logger格式: [" + length + "]有误，无法将其转换为数值类型。");
            throw e;
        }
    }

    public static String formatLog(List<Pattern> patternList, String msg, LogLevel level) {
        StringBuilder buffer = new StringBuilder();
        for (Pattern pattern : patternList) {
            if (pattern.getConverter() != null) {
                if (pattern.getText().equals(LOG_KEY_WORD_MSG)) {
                    buffer.append(pattern.getConverter().convert(msg, pattern.getFormat()));
                    continue;
                }
                if (pattern.getText().equals(LOG_KEY_WORD_LEVEL)) {
                    buffer.append(pattern.getConverter().convert(level.toString(), pattern.getFormat()));
                    continue;
                }
                buffer.append(pattern.getConverter().convert(pattern.getText(), pattern.getFormat()));
            } else {
                buffer.append(pattern.getText());
            }

        }
        return "\n" + buffer;
    }
}
