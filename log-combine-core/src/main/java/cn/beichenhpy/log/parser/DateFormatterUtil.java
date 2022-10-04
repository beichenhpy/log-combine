package cn.beichenhpy.log.parser;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static cn.beichenhpy.log.LogCombineConstants.DEFAULT_DATETIME_FORMATTER;
import static cn.beichenhpy.log.LogCombineConstants.DEFAULT_DATETIME_FORMATTER_STR;

/**
 * 日期转换工具类
 *
 * @author beichenhpy
 * <p> 2022/10/3 21:24
 */
public class DateFormatterUtil {

    public static final Map<String, DateTimeFormatter> DATE_TIME_FORMATTER_CACHE = new HashMap<>(8);

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

}
