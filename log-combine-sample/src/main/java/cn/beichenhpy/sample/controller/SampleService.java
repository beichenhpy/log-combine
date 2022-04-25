package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.annotation.LogCombine;
import cn.beichenhpy.log.context.LogCombineHelper;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *
 * </pre>
 *
 * @author beichenhpy
 * <p> 2022/4/24 09:07
 */
@Service
public class SampleService {

    @LogCombine
    public void test2() {
        LogCombineHelper.info("service:{}", 2);
        test3();
    }


    private void test3() {
        LogCombineHelper.debug("test3:{}", "test333");
    }
}
