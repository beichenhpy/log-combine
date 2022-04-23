package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.annotation.LogCombine;
import cn.beichenhpy.log.context.LogCombineHelper;
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
@RestController
public class SampleController {


    @LogCombine
    @GetMapping("/1")
    public void test() {
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
        System.out.println(className);
        LogCombineHelper.info("测试:{},{}", 1, 2);
        new Thread(
                () -> LogCombineHelper.info("测试2:{},{}", 3, 4)
        ).start();
    }
}
