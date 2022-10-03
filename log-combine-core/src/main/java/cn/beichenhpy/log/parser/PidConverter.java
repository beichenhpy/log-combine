package cn.beichenhpy.log.parser;

import cn.beichenhpy.log.LogCombineConstants;

import java.lang.management.ManagementFactory;

/**
 * @author beichenhpy
 * <p> 2022/10/2 19:04
 */
public class PidConverter implements Converter {


    @Override
    public String convert(String value, String format) {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String[] names = name.split(LogCombineConstants.AT);
        return names[0];
    }
}
