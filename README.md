# log-combine

🎆 现在支持异步非阻塞线程使用啦！  
现有的打印方式改为，同步线程日志打印，异步日志线程单独打印出来。

```text
2022-05-08 10:50:02.315  INFO 20390 --- [nio-8080-exec-6] combine-log have generated               : 
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] INFO cn.beichenhpy.sample.controller.SampleController - [71] - test:1,2
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleController - [72] - test2:3,4
2022-05-08 10:50:02,314 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleService - [43] - test3:test333
2022-05-08 10:50:06.318  INFO 20390 --- [pool-4-thread-1] combine-log have generated               : 
2022-05-08 10:50:02,314 - [pool-4-thread-1] DEBUG cn.beichenhpy.sample.controller.SampleController - [79] - test3:5
```

## 一个用于合并打印日志的工具包

**注意！**  
不要混用以下的两种打印日志的方式，不然会有意外的错误发生。如果你使用spring相关  
starter包中的aop会自动帮你打印日志，前提是在方法上添加了`@LogConbime`注解。

## 使用方式

你可以参考 `log-combine-sample`,这个项目是一个简单的实现。

### 如果你不使用Spring框架

1. 添加依赖到pom

```xml

<dependency>
    <groupId>cn.beichenhpy</groupId>
    <artifactId>log-combine-core</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. 在方法中使用 `LogCombineHelper.info/error/..` 并且用 `LogCombineHelper.print`打印日志。

```java

class Test {
    public void test() {
        // do something
        LogCombineHelper.info("this is a test method, you can use it like {}", "logback");
        //print your logs
        LogCombineHelper.print();
    }
}

```

3. 【new】如果你想在异步线程中使用，请手动在异步线程的方法中调用 `LogCombineHelper.print()` 以实现异步线程的打印。

```java
class Test2 {
    @GetMapping("/no-spring")
    @SneakyThrows
    public void test2() {
        LogCombineHelper.info("test:{},{}", 1, 2);
        LogCombineHelper.debug("test2:{},{}", 3, 4);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(
                () -> {
                    LogCombineHelper.debug("test3:{}", 5);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //手动调用打印方法
                    LogCombineHelper.print();
                }
        );
        //手动打印
        LogCombineHelper.print();
    }
}
```

### 如果你使用Spring框架

1. 添加到你的依赖

```xml

<dependency>
    <groupId>cn.beichenhpy</groupId>
    <artifactId>log-combine-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. 在程序入口添加 `@EnableLogCombine` 注解
3. 在你要合并打印的方法上添加 `@LogCombine` 然后使用 `LogCombineHelper` 的静态方法添加日志

```java

class Test {
    @LogCombine
    public void test() {
        // do something
        LogCombineHelper.info("this is a test method, you can use it like {}", "logback");
    }
}

```

4. 【new】如果你想在异步线程中使用，请手动在异步线程的方法中调用 `LogCombineHelper.print()` 以实现异步线程的打印 或者你可以像调用方法一样正常使用，前提是需要在方法上添加 `@LogCombine`
   注解。【两者不能混用】

```java


class Test3 {

   @Resource
   private SampleService sampleService;

   @LogCombine
   @GetMapping("/spring")
   @SneakyThrows
   public void test() {
      LogCombineHelper.info("test:{},{}", 1, 2);
      LogCombineHelper.debug("test2:{},{}", 3, 4);
      executorService.execute(
              //注解形式使用
              () -> sampleService.test2()
      );
      executorService.execute(
              //直接调用
              () -> {
                 LogCombineHelper.info("service:{}", 2);
                 LogCombineHelper.print();
              }
      );
   }
}

@Service
class SampleService {
   @LogCombine
   public void test2() {
      LogCombineHelper.info("service:{}", 2);
   }
}
```
