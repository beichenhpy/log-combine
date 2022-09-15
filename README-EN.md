# log-combine

🎆 Asynchronous non-blocking threads are now supported!  
The print result is as follows:

```text
2022-05-08 10:50:02.315  INFO 20390 --- [nio-8080-exec-6] c.b.log.context.LogCombineUtil : 
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] INFO cn.beichenhpy.sample.controller.SampleController - [71] - test:1,2
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleController - [72] - test2:3,4
2022-05-08 10:50:02,314 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleService - [43] - test3:test333
2022-05-08 10:50:06.318  INFO 20390 --- [pool-4-thread-1] c.b.log.context.LogCombineUtil : 
2022-05-08 10:50:02,314 - [pool-4-thread-1] DEBUG cn.beichenhpy.sample.controller.SampleController - [79] - test3:5
```

【notice】 📢:`LogCombineUtil.pushNest()`and`LogCombineUtil.popNest()`can not mixed `@LogCombine`

【feature】

1. Supports merge printing of logs for asynchronous threads
2. Support for generating merged logs based on user-set log levels

## A tool for combined log printing in one method.

**NOTICE**  
Do not mix the following two methods, otherwise unexpected errors will occur

## how to use

you can see `log-combine-sample`, this is a simple example.

### if you do not use spring framework

1. add dependence to your pom

```xml

<dependency>
    <groupId>cn.beichenhpy</groupId>
    <artifactId>log-combine-core</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

2. use `LogCombineUtil.info/error/..` in a method and use `LogCombineUtil.print`

```java

class Test {
   public void test() {
      // do something
      LogCombineUtil.info("this is a test method, you can use it like {}", "logback");
      //print your logs
      LogCombineUtil.print();
   }
}

```

3. if you nest `LogCombineUtil.print()`, then you need use `LogCombineUtil.pushNest()`
   and `LogCombineUtil.popNest()`

example：

```java
class Test0 {
   @SneakyThrows
   public void test2() {
      LogCombineUtil.info("test:{},{}", 1, 2);
      LogCombineUtil.warn("test2:{},{}", 3, 4);
      //nest
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
                 //There is nesting here, if you call the pushNest method before the method and then call popNest, 
                 // it will cause the after nest print log to split
                 LogCombineUtil.pushNest();
                 sampleService.test3();
                 LogCombineUtil.popNest();
                 LogCombineUtil.info("after nest print:{}", "after");
                 LogCombineUtil.print();
              }
      );
      LogCombineUtil.print();
   }
}
```

4. 【new】If you want to use it in a non-blocking thread, call it manually in the method of the non-blocking thread
   `LogCombineUtil.print()` to Implement log combine printing。

```java
class Test2 {

   @SneakyThrows
   public void test2() {
      LogCombineUtil.info("test:{},{}", 1, 2);
      LogCombineUtil.debug("test2:{},{}", 3, 4);
      new Thread(
              () -> {
                 LogCombineUtil.debug("test3:{}", 5);
                 try {
                    Thread.sleep(4000);
                 } catch (InterruptedException e) {
                    e.printStackTrace();
                 }
                 //manual print
                 LogCombineUtil.print();
              }
      ).start();
      //manual print
      LogCombineUtil.print();
   }
}
```

### if you use spring framework

1. add dependence to your pom

```xml

<dependency>
    <groupId>cn.beichenhpy</groupId>
    <artifactId>log-combine-starter</artifactId>
    <version>0.0.2-SNAPSHOT</version>
</dependency>
```

2. add `@EnableLogCombine` to your application Entrance
3. add `@LogCombine` on your method and use `LogCombineUtil` to add log info

```java

class Test {
    @LogCombine
    public void test() {
        // do something
        LogCombineUtil.info("this is a test method, you can use it like {}", "logback");
    }
}

```

4. 【new】If you want to use it in a non-blocking thread, call `LogCombineUtil.print()` manually in the method of the
   non-blocking thread to enable printing in the non-blocking thread.Or you can use it normally as if you were calling a
   method, provided you add the `@LogCombine` annotation to the method。【The two ways cannot be mixed】

```java


class Test3 {

    @Resource
    private SampleService sampleService;

   @LogCombine
   @GetMapping("/spring")
   @SneakyThrows
   public void test() {
      LogCombineUtil.info("test:{},{}", 1, 2);
      LogCombineUtil.debug("test2:{},{}", 3, 4);
      executorService.execute(
              //LogCombine annotation
              () -> sampleService.test2()
      );
      executorService.execute(
              //manual print
              () -> {
                 LogCombineUtil.info("service:{}", "manual print");
                 LogCombineUtil.print();
              }
      );
   }
}

@Service
class SampleService {
   @LogCombine
   public void test2() {
      LogCombineUtil.info("service:{}", 2);
   }
}
```