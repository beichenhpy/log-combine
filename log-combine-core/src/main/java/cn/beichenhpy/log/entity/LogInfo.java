package cn.beichenhpy.log.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
     * 消息列表
     */
    private List<String> messages;

    /**
     * 递归层数
     */
    private int nestedFloor;
}
