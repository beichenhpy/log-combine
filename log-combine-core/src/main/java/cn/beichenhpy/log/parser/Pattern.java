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

    private int type;

    private String format;

    private Converter converter;
}
