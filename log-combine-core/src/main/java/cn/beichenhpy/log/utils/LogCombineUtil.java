package cn.beichenhpy.log.utils;

import cn.beichenhpy.log.entity.ParsedPattern;
import cn.beichenhpy.log.enums.LogLevel;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
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
    public static final String STRING_PATTERN = "%s";
    public static final String PERCENT = "%";
    public static final String AT = "@";
    public static final String DOT = ".";
    public static final String DOT_REGEX = "\\.";
    public static final String LF = "\n";
    public static final String CURLY = "{}";

    /**
     * key word
     */
    public static final String LOG_KEY_WORD_PID = "pid";
    public static final String LOG_KEY_WORD_THREAD = "thread";
    public static final String LOG_KEY_WORD_LEVEL = "level";
    public static final String LOG_KEY_WORD_LINE = "line";
    public static final String LOG_KEY_WORD_MSG = "msg";
    public static final String LOG_KEY_WORD_DATE = "date";
    public static final String LOG_KEY_WORD_DATE_REGEX = "date\\{(.*?)}";
    public static final String LOG_KEY_WORD_LOGGER = "logger";
    public static final String LOG_KEY_WORD_LOGGER_REGEX = "logger\\{(.*?)}";


    /**
     * 获取调用本方法的行 <br>
     *
     * @return 行数
     */
    private static Supplier<Object> getInvokeLineNumberSupplier() {
        return () -> Thread.currentThread().getStackTrace()[5].getLineNumber();
    }


    /**
     * 获取调用本方法的行 <br>
     *
     * @return 行数
     */
    private static Supplier<Object> getInvokeClassNameSupplier() {
        return () -> Thread.currentThread().getStackTrace()[5].getClassName();
    }

    /**
     * 获取当前线程名
     *
     * @return 线程名
     */
    private static Supplier<Object> getThreadNameSupplier() {
        return () -> Thread.currentThread().getName();
    }

    /**
     * 获取当前进程id
     *
     * @return 返回id
     */
    private static Supplier<Object> getPidSupplier() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] names = name.split(AT);
        return () -> Integer.valueOf(names[0]);
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
        if (originLength <= length || length <= 0) {
            return origin;
        }
        String[] originItems = origin.split(DOT_REGEX);
        String[] curtailItems = new String[originItems.length];
        System.arraycopy(originItems, 0, curtailItems, 0, originItems.length);
        for (int i = 0; i < originItems.length - 1; i++) {
            curtailItems[i] = originItems[i].substring(0, 1);
            int currentLength = originLength - (originItems[i].length() - 1);
            if (currentLength <= length) {
                return String.join(DOT, curtailItems);
            }
        }
        return String.join(DOT, curtailItems);
    }


    /**
     * 编译期解析
     */
    public static ParsedPattern parsePattern(String pattern) {
        ParsedPattern parsedPattern = new ParsedPattern();
        List<String> keywords = new LinkedList<>();
        Map<String, Supplier<Object>> keywordAndSupplierMap = new HashMap<>(4);
        StringBuilder logFormatBuilder = new StringBuilder();
        String[] patternArrays = pattern.split(PERCENT);
        for (String item : patternArrays) {
            if (item.startsWith(LOG_KEY_WORD_DATE)) {
                //date
                item = parseDate(parsedPattern, item);
                keywords.add(LOG_KEY_WORD_DATE);
            } else if (item.startsWith(LOG_KEY_WORD_LOGGER)) {
                //logger
                item = parseLogger(parsedPattern, item);
                keywords.add(LOG_KEY_WORD_LOGGER);
                keywordAndSupplierMap.put(LOG_KEY_WORD_LOGGER, getInvokeClassNameSupplier());
            } else if (item.startsWith(LOG_KEY_WORD_LEVEL)) {
                //level
                item = item.replace(LOG_KEY_WORD_LEVEL, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_LEVEL);
            } else if (item.startsWith(LOG_KEY_WORD_LINE)) {
                //line
                item = item.replace(LOG_KEY_WORD_LINE, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_LINE);
                keywordAndSupplierMap.put(LOG_KEY_WORD_LINE, getInvokeLineNumberSupplier());
            } else if (item.startsWith(LOG_KEY_WORD_THREAD)) {
                //thread
                item = item.replace(LOG_KEY_WORD_THREAD, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_THREAD);
                keywordAndSupplierMap.put(LOG_KEY_WORD_THREAD, getThreadNameSupplier());
            } else if (item.startsWith(LOG_KEY_WORD_MSG)) {
                //msg
                item = item.replace(LOG_KEY_WORD_MSG, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_MSG);
            } else if (item.startsWith(LOG_KEY_WORD_PID)) {
                //pid
                item = item.replace(LOG_KEY_WORD_PID, STRING_PATTERN);
                keywords.add(LOG_KEY_WORD_PID);
                keywordAndSupplierMap.put(LOG_KEY_WORD_PID, getPidSupplier());
            }
            //todo 考虑扩展性 避免大量的if-else
            logFormatBuilder.append(item);
        }
        parsedPattern.setLogFormat(logFormatBuilder.toString());
        parsedPattern.setKeyWords(keywords);
        parsedPattern.setKeywordAndSupplierMap(keywordAndSupplierMap);
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
            parsedPattern.getDateTimeFormatters().addLast(DateTimeFormatter.ofPattern(matcher.group(1)));
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
            parsedPattern.getLoggerLengths().addLast(Integer.valueOf(matcher.group(1)));
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
    public static String formatLog(ParsedPattern parsedPattern, String msg, LogLevel level) {
        final Deque<DateTimeFormatter> dateTimeFormatters = parsedPattern.getDateTimeFormatters();
        final Deque<Integer> loggerLengths = parsedPattern.getLoggerLengths();
        final List<String> keyWords = parsedPattern.getKeyWords();
        final Map<String, Supplier<Object>> keywordAndSupplierMap = parsedPattern.getKeywordAndSupplierMap();
        final Object[] args = new Object[keyWords.size()];
        int i = 0;
        for (String keyWord : keyWords) {
            switch (keyWord) {
                case LOG_KEY_WORD_DATE:
                    DateTimeFormatter formatter = dateTimeFormatters.poll();
                    if (formatter == null) {
                        formatter = DEFAULT_DATE_FORMATTER;
                    }
                    args[i] = formatter.format(LocalDateTime.now());
                    i++;
                    //重新放入队尾
                    dateTimeFormatters.addLast(formatter);
                    break;
                case LOG_KEY_WORD_THREAD:
                    args[i] = keywordAndSupplierMap.get(LOG_KEY_WORD_THREAD).get();
                    i++;
                    break;
                case LOG_KEY_WORD_LEVEL:
                    args[i] = level;
                    i++;
                    break;
                case LOG_KEY_WORD_LINE:
                    args[i] = keywordAndSupplierMap.get(LOG_KEY_WORD_LINE).get();
                    i++;
                    break;
                case LOG_KEY_WORD_LOGGER:
                    Integer length = loggerLengths.poll();
                    if (length == null) {
                        length = DEFAULT_LOGGER_LENGTH;
                    }
                    args[i] = curtailReference((String) keywordAndSupplierMap.get(LOG_KEY_WORD_LOGGER).get(), length);
                    i++;
                    loggerLengths.addLast(length);
                    break;
                case LOG_KEY_WORD_MSG:
                    args[i] = msg;
                    i++;
                    break;
                case LOG_KEY_WORD_PID:
                    args[i] = keywordAndSupplierMap.get(LOG_KEY_WORD_PID).get();
                    i++;
                    break;
            }
        }
        return String.format(LF + parsedPattern.getLogFormat(), args);
    }

}
