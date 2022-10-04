package cn.beichenhpy.log.parser;

import cn.beichenhpy.log.enums.LogLevel;

import java.util.List;

import static cn.beichenhpy.log.LogCombineConstants.*;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:19
 */
public class ParseUtil {


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
                    buffer.append(pattern.getConverter().convert(msg, pattern.getOption()));
                    continue;
                }
                if (pattern.getText().equals(LOG_KEY_WORD_LEVEL)) {
                    buffer.append(pattern.getConverter().convert(level.toString(), pattern.getOption()));
                    continue;
                }
                buffer.append(pattern.getConverter().convert(pattern.getText(), pattern.getOption()));
            } else {
                buffer.append(pattern.getText());
            }

        }
        return "\n" + buffer;
    }
}
