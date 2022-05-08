# log-combine

NOW **NOT SUPPORT** ASYNC NON-BLOCKING THREAD

**⚠WARNING**  
if you use `LogCombineHelper` in non-blocking thread like  `runnable`  
it maybe effects "ghost log", because a non-blocking thread method can not be controlled.

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
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. use `LogCombineHelper.info/error/..` in a method and use `LogCombineHelper.print`

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

3. 【new】If you want to use it in a non-blocking thread, call it manually in the method of the non-blocking thread
   `LogCombineHelper.print()` to Implement log combine printing。

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
                    //manual print
                    LogCombineHelper.print();
                }
        );
        //manual print
        LogCombineHelper.print();
    }
}
```

### if you use spring framework

1. add dependence to your pom

```xml

<dependency>
    <groupId>cn.beichenhpy</groupId>
    <artifactId>log-combine-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. add `@EnableLogCombine` to your application Entrance
3. add `@LogCombine` on your method and use `LogCombineHelper` to add log info

```java

class Test {
    @LogCombine
    public void test() {
        // do something
        LogCombineHelper.info("this is a test method, you can use it like {}", "logback");
    }
}

```

4. 【new】If you want to use it in a non-blocking thread, call `LogCombineHelper.print()` manually in the method of the
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
      LogCombineHelper.info("test:{},{}", 1, 2);
      LogCombineHelper.debug("test2:{},{}", 3, 4);
      executorService.execute(
              //LogCombine annotation
              () -> sampleService.test2()
      );
      executorService.execute(
              //manual print
              () -> {
                 LogCombineHelper.info("service:{}", "manual print");
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