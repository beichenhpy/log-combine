package cn.beichenhpy.log.entity;

import cn.beichenhpy.log.utils.LogCombineUtil;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.Map;

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
     * 日期格式
     *
     * @see cn.beichenhpy.log.utils.LogCombineUtil#DEFAULT_DATE_FORMAT
     */
    private String dateFormat = LogCombineUtil.DEFAULT_DATE_FORMAT;

    /**
     * 日期格式转换
     */
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LogCombineUtil.DEFAULT_DATE_FORMAT);

    /**
     * 类名的长度限制,默认不限制长度
     *
     * @see cn.beichenhpy.log.utils.LogCombineUtil#DEFAULT_CLASS_LENGTH
     */
    private int classLength = LogCombineUtil.DEFAULT_CLASS_LENGTH;

    /**
     * 日志格式关键字及对应的顺序 没有则为-1
     */
    private Map<String, Integer> keyWordAndOrder = LogCombineUtil.DEFAULT_KEYWORD_ORDER;
}
