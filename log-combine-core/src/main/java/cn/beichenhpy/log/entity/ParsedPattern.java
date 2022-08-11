package cn.beichenhpy.log.entity;

import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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
    private String logFormat;

    /**
     * 日期格式转换队列 支持多个
     */
    private Deque<DateTimeFormatter> dateTimeFormatters = new LinkedList<>();

    /**
     * 类名的长度限制 支持多个
     */
    private Deque<Integer> loggerLengths = new LinkedList<>();

    /**
     * 日志格式关键字及对应的顺序
     */
    private List<String> keyWords;

    /**
     * pattern获取对应的值需要的哈希
     */
    private Map<String, Supplier<Object>> keywordAndSupplierMap;
}
