package cn.beichenhpy.log.entity;

import cn.beichenhpy.log.utils.LogCombineUtil;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 翻译后的日志模式格式
 *
 * @author beichenhpy
 * @since 0.0.2
 */
@Data
public class ParsedPattern {
    /**
     * 解析后的格式,例如
     * <pre>
     *     %s - [%s] %s %s - [%s] - %s
     * </pre>
     */
    private String logFormat = LogCombineUtil.DEFAULT_LOG_FORMAT;

    /**
     * 日期格式转换队列 支持多个
     */
    private Queue<DateTimeFormatter> dateTimeFormatters = new LinkedList<>();

    /**
     * 类名的长度限制 支持多个
     */
    private Queue<Integer> classLengths = new LinkedList<>();

    /**
     * 日志格式关键字及对应的顺序
     */
    private List<String> keyWords = new LinkedList<>(
            Arrays.asList(LogCombineUtil.LOG_KEY_WORD_DATE, LogCombineUtil.LOG_KEY_WORD_THREAD, LogCombineUtil.LOG_KEY_WORD_LEVEL,
                    LogCombineUtil.LOG_KEY_WORD_CLASS, LogCombineUtil.LOG_KEY_WORD_LINE, LogCombineUtil.LOG_KEY_WORD_MSG)
    );
}
