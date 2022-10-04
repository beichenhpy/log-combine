package cn.beichenhpy.log.parser;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:58
 */
public class ThreadConverter implements Converter {

    @Override
    public String convert(String value, String option) {
        return Thread.currentThread().getName();
    }
}
