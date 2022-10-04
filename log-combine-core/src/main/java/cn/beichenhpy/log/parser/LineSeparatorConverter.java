package cn.beichenhpy.log.parser;

import cn.beichenhpy.log.LogCombineConstants;

/**
 * @author beichenhpy
 * <p> 2022/10/3 20:56
 */
public class LineSeparatorConverter implements Converter {

    @Override
    public String convert(String value, String option) {
        return LogCombineConstants.LINE_SEPARATOR;
    }
}
