package cn.beichenhpy.log.parser;

/**
 * @author beichenhpy
 * <p> 2022/10/2 16:03
 */
public interface Converter {

    /**
     * 转换
     *
     * @param value  输入值
     * @param option 格式化选项
     * @return 返回转换后的字符串
     */
    String convert(String value, String option);
}
