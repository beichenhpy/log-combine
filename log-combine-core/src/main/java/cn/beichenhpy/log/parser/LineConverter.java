package cn.beichenhpy.log.parser;

/**
 * @author beichenhpy
 * <p> 2022/10/2 19:01
 */
public class LineConverter implements Converter {
    @Override
    public String convert(String value, String format) {
        return String.valueOf(Thread.currentThread().getStackTrace()[5].getLineNumber());
    }
}
