package cn.beichenhpy.log.parser;

import java.lang.management.ManagementFactory;

/**
 * @author beichenhpy
 * <p> 2022/10/2 19:04
 */
public class PidConverter implements Converter {

    public static final String AT = "@";

    @Override
    public String convert(String text, String format) {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] names = name.split(AT);
        return names[0];
    }
}
