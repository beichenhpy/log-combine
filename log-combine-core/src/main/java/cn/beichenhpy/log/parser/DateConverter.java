package cn.beichenhpy.log.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:28
 */
public class DateConverter implements Converter {

    @Override
    public String convert(String value, String option) {
        DateTimeFormatter df = DateFormatterUtil.getDateTimeFormatter(option);
        return df.format(LocalDateTime.now());
    }
}
