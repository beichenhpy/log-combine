package cn.beichenhpy.log.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:28
 */
public class DateConverter implements Converter {

    @Override
    public String convert(String value, String format) {
        DateTimeFormatter df = DateFormatterUtil.getDateTimeFormatter(format);
        return df.format(LocalDateTime.now());
    }
}
