package cn.beichenhpy.log.parser;

/**
 * @author beichenhpy
 * <p> 2022/10/3 20:56
 */
public class LineSeparatorConverter implements Converter {

    @Override
    public String convert(String text, String format) {
        return ConvertUtil.LINE_SEPARATOR;
    }
}
