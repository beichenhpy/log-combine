package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.annotation.LogCombine;
import cn.beichenhpy.log.context.LogCombineHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 *
 * </PRE>
 * CREATE_TIME: 2022/4/23 16:47
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@Slf4j
@RestController
public class SampleController {


    @LogCombine
    @GetMapping("/1")
    public void test() {
        LogCombineHelper.info("测试:{},{}", 1, 2);
        LogCombineHelper.debug("测试2:{},{}", 3, 4);
    }
}
