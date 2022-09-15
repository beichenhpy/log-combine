/*
 * Copyright 2022 韩鹏宇
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package cn.beichenhpy.sample.controller;

import cn.beichenhpy.log.LogCombineUtil;
import cn.beichenhpy.log.annotation.LogCombine;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    private SampleService sampleService;

    @LogCombine
    @GetMapping("/spring")
    public void test() throws ExecutionException, InterruptedException {
        LogCombineUtil.debug("test:{},{}", 1, 2);
        LogCombineUtil.warn("test2:{},{}", 3, 4);
        //nest
        sampleService.test2();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //support but you need to manual print or use method with @LogCombine
        executorService.execute(
                () -> sampleService.test2()
        );
        executorService.execute(
                //直接调用
                () -> {
                    LogCombineUtil.info("service:{}", "direct invoke");
                    LogCombineUtil.print();
                }
        );
        //but if you use submit and get result ,it works.
        Future<?> task = executorService.submit(
                () -> LogCombineUtil.debug("test3:{}", 5)
        );
        task.get();
    }


    @GetMapping("/no-spring")
    @SneakyThrows
    public void test2() {
        LogCombineUtil.info("test:{},{}", 1, 2);
        LogCombineUtil.warn("test2:{},{}", 3, 4);
        //LogCombine nested
        LogCombineUtil.pushNest();
        sampleService.test3();
        LogCombineUtil.popNest();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //support but you need to manual print
        executorService.execute(
                () -> {
                    LogCombineUtil.warn("test3:{}", 5);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LogCombineUtil.pushNest();
                    sampleService.test3();
                    LogCombineUtil.popNest();
                    LogCombineUtil.info("after nest print:{}", "after");
                    LogCombineUtil.print();
                }
        );
        //but if you use submit and get result ,it works.
        Future<?> task = executorService.submit(
                () -> LogCombineUtil.debug("test3:{}", 5)
        );
        task.get();
        //manual print
        LogCombineUtil.print();
    }

    @GetMapping("parallel-test")
    @LogCombine
    public void parallelTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(
                () -> {
                    LogCombineUtil.info("1test1");
                    LogCombineUtil.info("1test1");
                    LogCombineUtil.info("1test1");
                    LogCombineUtil.print();
                }
        );
        executorService.execute(
                () -> {
                    LogCombineUtil.info("2test2");
                    LogCombineUtil.info("2test2");
                    LogCombineUtil.info("2test2");
                    LogCombineUtil.info("2test2");
                    LogCombineUtil.print();
                }
        );
    }
}
