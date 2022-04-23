package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.annotation.LogCombine;
import cn.beichenhpy.log.context.LogCombineHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public void test() throws ExecutionException, InterruptedException {
        LogCombineHelper.info("test:{},{}", 1, 2);
        LogCombineHelper.debug("test2:{},{}", 3, 4);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //can not record log to context, because This operation is asynchronous and non-blocking. AOP can't wait.
        executorService.execute(
                () -> LogCombineHelper.debug("test3:{}", 5)
        );
        //but if you use submit and get result ,it works.
        Future<?> task = executorService.submit(
                () -> LogCombineHelper.debug("test3:{}", 5)
        );
        task.get();
    }
}
