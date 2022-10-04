package cn.beichenhpy.log.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author beichenhpy
 * <p> 2022/10/2 16:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pattern {

    private String text;

    //类型 关键字或文字
    private int type;

    //附加的一些选项 在 {} 中配置
    private String option;

    private Converter converter;
}
