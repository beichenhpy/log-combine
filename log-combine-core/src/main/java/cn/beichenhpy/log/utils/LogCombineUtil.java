package cn.beichenhpy.log.utils;

import cn.beichenhpy.log.entity.ParsedPattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author beichenhpy
 * @since 0.0.1
 */
public class LogCombineUtil {
    public static final String DEFAULT_PATTERN = "%date{yyyy-MM-dd HH:mm:ss,SSS}  %level %pid --- [%thread]  %logger{35} - [%line] :%msg";
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");
    public static final Integer DEFAULT_LOGGER_LENGTH = 35;
    /**
     * key word
     */
    public static final String STRING_PATTERN = "%s";
    public static final String LOG_KEY_WORD_PID = "pid";
    public static final String LOG_KEY_WORD_DATE = "date";
    public static final String LOG_KEY_WORD_DATE_REGEX = "date\\{(.*?)}";
    public static final String LOG_KEY_WORD_THREAD = "thread";
    public static final String LOG_KEY_WORD_LEVEL = "level";
    public static final String LOG_KEY_WORD_LOGGER = "logger";
    public static final String LOG_KEY_WORD_LOGGER_REGEX = "logger\\{(.*?)}";
    public static final String LOG_KEY_WORD_LINE = "line";
    public static final String LOG_KEY_WORD_MSG = "msg";

    /**
     * 根据指定长度缩短全限定类名的包名部分
     * <p> 缩短包名,直到全限定类名到达指定的长度，
     * 如果已经缩短到最后的类还没有到达指定的长度则不再缩短
     *
     * @param origin 原数据
     * @param length 长度限定
     * @return 缩短后的全限定类名
     */
    public static String curtailReference(String origin, int length) {
        if (origin == null) {
            return null;
        }
        int originLength = origin.length();
        if (originLength <= length || length <= 0) {
            return origin;
        }
        String[] originItems = origin.split("\\.");
        String[] curtailItems = new String[originItems.length];
        System.arraycopy(originItems, 0, curtailItems, 0, originItems.length);
        for (int i = 0; i < originItems.length - 1; i++) {
            curtailItems[i] = originItems[i].substring(0, 1);
            int currentLength = originLength - (originItems[i].length() - 1);
            if (currentLength <= length) {
                return String.join(".", curtailItems);
            }
        }
        return String.join(".", curtailItems);
    }


    /**
     * 编译期解析
     */
    public static ParsedPattern parsePattern(String pattern) {
        ParsedPattern parsedPattern = new ParsedPattern();
        List<String> keywords = new LinkedList<>();
        String[] patternArrays = pattern.split("%");
        StringBuilder logFormatBuilder = new StringBuilder();
        for (String patternArray : patternArrays) {
            String item = patternArray;
            if (item.startsWith(LOG_KEY_WORD_DATE)) {
                //date
                item = parseDate(parsedPattern, item);
                keywords.add(LOG_KEY_WORD_DATE);
            } else if (item.startsWith(LOG_KEY_WORD_LOGGER)) {
                //logger
                item = parseLogger(parsedPattern, item);
                keywords.add(LOG_KEY_WORD_LOGGER);
            } else if (item.startsWith(LOG_KEY_WORD_LEVEL)) {
                //level
                item = item.replace(LOG_KEY_WORD_LEVEL, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_LEVEL);
            } else if (item.startsWith(LOG_KEY_WORD_LINE)) {
                //line
                item = item.replace(LOG_KEY_WORD_LINE, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_LINE);
            } else if (item.startsWith(LOG_KEY_WORD_THREAD)) {
                //thread
                item = item.replace(LOG_KEY_WORD_THREAD, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_THREAD);
            } else if (item.startsWith(LOG_KEY_WORD_MSG)) {
                //msg
                item = item.replace(LOG_KEY_WORD_MSG, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_MSG);
            } else if (item.startsWith(LOG_KEY_WORD_PID)) {
                //pid
                item = item.replace(LOG_KEY_WORD_PID, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_PID);
            }
            logFormatBuilder.append(item);
        }
        parsedPattern.setLogFormat(logFormatBuilder.toString());
        parsedPattern.setKeyWords(keywords);
        return parsedPattern;
    }

    /**
     * 设置日期格式化格式，返回解析后的样式
     *
     * @param parsedPattern 解析后的样式
     * @param item          日期的样式
     * @return 返回日期的样式格式化后的字符串
     */
    private static String parseDate(ParsedPattern parsedPattern, String item) {
        //date
        Pattern datePattern = Pattern.compile(LOG_KEY_WORD_DATE_REGEX);
        Matcher matcher = datePattern.matcher(item);
        if (matcher.find()) {
            parsedPattern.getDateTimeFormatters().add(DateTimeFormatter.ofPattern(matcher.group(1)));
            item = item.replace(matcher.group(), STRING_PATTERN);
        } else {
            //default format
            item = item.replace(LOG_KEY_WORD_DATE, STRING_PATTERN);
        }
        return item;
    }

    /**
     * 设置类名的长度， 返回解析后的样式
     *
     * @param parsedPattern 解析后的样式
     * @param item          类名的样式
     * @return 返回类名的样式格式化后的字符串
     */
    private static String parseLogger(ParsedPattern parsedPattern, String item) {
        Pattern loggerPattern = Pattern.compile(LOG_KEY_WORD_LOGGER_REGEX);
        Matcher matcher = loggerPattern.matcher(item);
        if (matcher.find()) {
            parsedPattern.getLoggerLengths().add(Integer.valueOf(matcher.group(1)));
            item = item.replace(matcher.group(), STRING_PATTERN);
        } else {
            //default format
            item = item.replace(LOG_KEY_WORD_LOGGER, STRING_PATTERN);
        }
        return item;
    }


    /**
     * 格式化日志 运行期
     */
    public static String formatLog(ParsedPattern parsedPattern, Function<Boolean, Object> getMsg, Function<Boolean, Object> getLine, Function<Boolean, Object> getPid,
                                   Function<Boolean, Object> getLogLevel, Function<Boolean, Object> getLogger, Function<Boolean, Object> getThreadName) {
        final Queue<DateTimeFormatter> cloneDateFormatters = new LinkedList<>(parsedPattern.getDateTimeFormatters());
        final Queue<Integer> cloneLoggerLengths = new LinkedList<>(parsedPattern.getLoggerLengths());
        List<String> keyWords = parsedPattern.getKeyWords();
        Object[] args = new Object[keyWords.size()];
        int i = 0;
        for (String keyWord : keyWords) {
            switch (keyWord) {
                case LOG_KEY_WORD_DATE:
                    DateTimeFormatter formatter = cloneDateFormatters.poll();
                    if (formatter == null) {
                        formatter = DEFAULT_DATE_FORMATTER;
                    }
                    args[i] = formatter.format(LocalDateTime.now());
                    i++;
                    break;
                case LOG_KEY_WORD_THREAD:
                    args[i] = getThreadName.apply(true);
                    i++;
                    break;
                case LOG_KEY_WORD_LEVEL:
                    args[i] = getLogLevel.apply(true);
                    i++;
                    break;
                case LOG_KEY_WORD_LINE:
                    args[i] = getLine.apply(true);
                    i++;
                    break;
                case LOG_KEY_WORD_LOGGER:
                    Integer length = cloneLoggerLengths.poll();
                    if (length == null) {
                        length = DEFAULT_LOGGER_LENGTH;
                    }
                    args[i] = curtailReference((String) getLogger.apply(true), length);
                    i++;
                    break;
                case LOG_KEY_WORD_MSG:
                    args[i] = getMsg.apply(true);
                    i++;
                    break;
                case LOG_KEY_WORD_PID:
                    args[i] = getPid.apply(true);
                    i++;
                    break;
            }
        }
        return String.format("\n" + parsedPattern.getLogFormat(), args);
    }

}
