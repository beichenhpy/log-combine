package cn.beichenhpy.log.parser;

import java.util.HashMap;
import java.util.Map;

import static cn.beichenhpy.log.parser.ParseUtil.*;

/**
 * @author beichenhpy
 * <p> 2022/10/3 20:46
 */
public class ConvertUtil {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final Map<String, Converter> KEYWORD_CONVERTER_CACHE = new HashMap<>(16);

    static {
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_DATE, new DateConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_LOGGER, new LoggerConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_PID, new PidConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_THREAD, new ThreadConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_LEVEL, new LevelConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_LINE, new LineConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_MSG, new MsgConverter());
        KEYWORD_CONVERTER_CACHE.put(LOG_KEY_WORD_LINE_SEPARATOR, new LineSeparatorConverter());
    }

    public static Converter getConverter(String key) {
        Converter converter = ConvertUtil.KEYWORD_CONVERTER_CACHE.get(key);
        if (converter == null) {
            throw new IllegalArgumentException("[LOG-COMBINE]: 您输入的关键字[" + key + "]不存在, 请确认是否输入正确。");
        }
        return converter;
    }
}
