package cn.beichenhpy.log.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <PRE>
 * 日志信息
 * </PRE>
 * CREATE_TIME: 2022/4/23 15:50
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInfo {

    /**
     * 输入的日志信息及占位符
     */
    private String format;

    /**
     * 输入的参数
     */
    private Object[] params;
}
