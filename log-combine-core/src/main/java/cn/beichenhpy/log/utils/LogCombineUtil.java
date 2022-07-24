package cn.beichenhpy.log.utils;

import cn.beichenhpy.log.Configuration;
import cn.beichenhpy.log.entity.ParsedPattern;
import cn.beichenhpy.log.enums.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author beichenhpy
 * @since 0.0.1
 */
public class LogCombineUtil {
    public static final String DEFAULT_PATTERN = "%date - [%thread] %level %class - [%line] - %msg";
    public static final String DEFAULT_LOG_FORMAT = "%s - [%s] %s %s - [%s] - %s";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final int DEFAULT_CLASS_LENGTH = -1;
    public static final Map<String, Integer> DEFAULT_KEYWORD_ORDER = new LinkedHashMap<>(8);
    public static final ParsedPattern DEFAULT_PARSED_PATTERN = new ParsedPattern();
    public static final Configuration DEFAULT_CONFIGURATION = new Configuration();
    /**
     * key word
     */
    protected static final String STRING_PATTERN = "%s";
    protected static final String LOG_KEY_WORD_DATE = "date";
    protected static final String LOG_KEY_WORD_DATE_REGEX = "date\\{(.*?)}";
    protected static final String LOG_KEY_WORD_THREAD = "thread";
    protected static final String LOG_KEY_WORD_LEVEL = "level";
    protected static final String LOG_KEY_WORD_CLASS = "class";
    protected static final String LOG_KEY_WORD_CLASS_REGEX = "class\\{(.*?)}";
    protected static final String LOG_KEY_WORD_LINE = "line";
    protected static final String LOG_KEY_WORD_MSG = "msg";

    static {
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_DATE, 0);
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_THREAD, 1);
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_LEVEL, 2);
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_CLASS, 3);
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_LINE, 4);
        DEFAULT_KEYWORD_ORDER.put(LOG_KEY_WORD_MSG, 5);
    }

    /**
     * 初始化空的关键字排序
     *
     * @return 返回空的关键字排序
     */
    private static Map<String, Integer> initEmptyKeywordOrder() {
        Map<String, Integer> empty = new LinkedHashMap<>();
        empty.put(LOG_KEY_WORD_DATE, -1);
        empty.put(LOG_KEY_WORD_THREAD, -1);
        empty.put(LOG_KEY_WORD_LEVEL, -1);
        empty.put(LOG_KEY_WORD_CLASS, -1);
        empty.put(LOG_KEY_WORD_LINE, -1);
        empty.put(LOG_KEY_WORD_MSG, -1);
        return empty;
    }

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
        if (originLength <= length || length == -1) {
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
     * 解析
     */
    public static ParsedPattern parsePattern(String pattern) {
        if (DEFAULT_PATTERN.equals(pattern)) {
            return DEFAULT_PARSED_PATTERN;
        }
        ParsedPattern parsedPattern = new ParsedPattern();
        Map<String, Integer> keywordAndOrder = initEmptyKeywordOrder();
        String[] patternArrays = pattern.split("%");
        StringBuilder logFormatBuilder = new StringBuilder();
        for (int i = 0; i < patternArrays.length; i++) {
            String item = patternArrays[i];
            if ("".equals(item)) {
                continue;
            }
            if (item.startsWith(LOG_KEY_WORD_DATE)) {
                //date
                item = parseDate(parsedPattern, item);
                keywordAndOrder.put(LOG_KEY_WORD_DATE, i);
            } else if (item.startsWith(LOG_KEY_WORD_CLASS)) {
                //class
                item = parseClass(parsedPattern, item);
                keywordAndOrder.put(LOG_KEY_WORD_CLASS, i);
            } else if (item.startsWith(LOG_KEY_WORD_LEVEL)) {
                //level
                item = item.replace(LOG_KEY_WORD_LEVEL, STRING_PATTERN);
                keywordAndOrder.put(LOG_KEY_WORD_LEVEL, i);
            } else if (item.startsWith(LOG_KEY_WORD_LINE)) {
                //line
                item = item.replace(LOG_KEY_WORD_LINE, STRING_PATTERN);
                keywordAndOrder.put(LOG_KEY_WORD_LINE, i);
            } else if (item.startsWith(LOG_KEY_WORD_THREAD)) {
                //thread
                item = item.replace(LOG_KEY_WORD_THREAD, STRING_PATTERN);
                keywordAndOrder.put(LOG_KEY_WORD_THREAD, i);
            } else if (item.startsWith(LOG_KEY_WORD_MSG)) {
                //msg
                item = item.replace(LOG_KEY_WORD_MSG, STRING_PATTERN);
                keywordAndOrder.put(LOG_KEY_WORD_MSG, i);
            }
            logFormatBuilder.append(item);
        }
        parsedPattern.setLogFormat(logFormatBuilder.toString());
        parsedPattern.setKeyWordAndOrder(keywordAndOrder);
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
            parsedPattern.setDateFormat(matcher.group(1));
            parsedPattern.setDateTimeFormatter(DateTimeFormatter.ofPattern(matcher.group(1)));
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
    private static String parseClass(ParsedPattern parsedPattern, String item) {
        Pattern datePattern = Pattern.compile(LOG_KEY_WORD_CLASS_REGEX);
        Matcher matcher = datePattern.matcher(item);
        if (matcher.find()) {
            parsedPattern.setClassLength(Integer.parseInt(matcher.group(1)));
            item = item.replace(matcher.group(), STRING_PATTERN);
        } else {
            //default format
            item = item.replace(LOG_KEY_WORD_CLASS, STRING_PATTERN);
        }
        return item;
    }


    public static String formatLog(ParsedPattern parsedPattern, String msg, Integer line, LogLevel level, String className, String threadName) {
        Map<String, Integer> keyWordAndOrder = parsedPattern.getKeyWordAndOrder();
        long size = keyWordAndOrder.values().stream()
                .filter(order -> order != -1)
                .count();
        if (size == 0) {
            return parsedPattern.getLogFormat();
        }
        Object[] args = new Object[(int) size];
        int i = 0;
        for (Map.Entry<String, Integer> entry : keyWordAndOrder.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (value == -1) {
                continue;
            }
            switch (key) {
                case LOG_KEY_WORD_DATE:
                    args[i] = parsedPattern.getDateTimeFormatter().format(LocalDateTime.now());
                    i++;
                    break;
                case LOG_KEY_WORD_THREAD:
                    args[i] = threadName;
                    i++;
                    break;
                case LOG_KEY_WORD_LEVEL:
                    args[i] = level.name();
                    i++;
                    break;
                case LOG_KEY_WORD_LINE:
                    args[i] = line.toString();
                    i++;
                    break;
                case LOG_KEY_WORD_CLASS:
                    args[i] = curtailReference(className, parsedPattern.getClassLength());
                    i++;
                    break;
                case LOG_KEY_WORD_MSG:
                    args[i] = msg;
                    i++;
                    break;
            }
        }
        return String.format("\n" + parsedPattern.getLogFormat(), args);
    }

}
