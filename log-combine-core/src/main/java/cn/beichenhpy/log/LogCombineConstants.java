package cn.beichenhpy.log;

import cn.beichenhpy.log.parser.ParserHelper;
import cn.beichenhpy.log.parser.Pattern;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author beichenhpy
 * <p> 2022/10/3 21:17
 */
public class LogCombineConstants {

    //默认
    public static final String DEFAULT_PATTERN = "%date{yyyy-MM-dd HH:mm:ss,SSS}  %level %pid --- [%thread]  %logger{35} - [%line] :%msg";
    public static final String DEFAULT_DATETIME_FORMATTER_STR = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_FORMATTER_STR);
    public static final List<Pattern> DEFAULT_PATTERN_LIST = new ParserHelper().parse(DEFAULT_PATTERN);
    //默认logger长度
    public static final int DEFAULT_LOGGER_LENGTH = 35;
    //关键字
    public static final String LOG_KEY_WORD_PID = "pid";
    public static final String LOG_KEY_WORD_THREAD = "thread";
    public static final String LOG_KEY_WORD_LEVEL = "level";
    public static final String LOG_KEY_WORD_LINE = "line";
    public static final String LOG_KEY_WORD_MSG = "msg";
    public static final String LOG_KEY_WORD_DATE = "date";
    public static final String LOG_KEY_WORD_LOGGER = "logger";
    public static final String LOG_KEY_WORD_LINE_SEPARATOR = "n";


    public static final String AT = "@";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
