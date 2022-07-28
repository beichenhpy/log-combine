# log-combine

🎆 新特性: 支持自定义日志格式

```text
2022-05-08 10:50:02.315  INFO 20390 --- [nio-8080-exec-6] c.b.log.context.LogCombineHelper : 
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] INFO cn.beichenhpy.sample.controller.SampleController - [71] - test:1,2
2022-05-08 10:50:02,313 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleController - [72] - test2:3,4
2022-05-08 10:50:02,314 - [http-nio-8080-exec-6] DEBUG cn.beichenhpy.sample.controller.SampleService - [43] - test3:test333
2022-05-08 10:50:06.318  INFO 20390 --- [pool-4-thread-1] c.b.log.context.LogCombineHelper : 
2022-05-08 10:50:02,314 - [pool-4-thread-1] DEBUG cn.beichenhpy.sample.controller.SampleController - [79] - test3:5
```
【注意】 📢:`LogCombineHelper.pushNest()`和`LogCombineHelper.popNest()`不可以与`@LogCombine`混用

【feature】

1. 支持异步线程的日志合并打印
2. 支持根据用户设置日志等级来生成合并的日志
3. 支持自定义日志格式

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
    <version>0.0.2-SNAPSHOT</version>
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

3. 如果遇到嵌套时，嵌套的内部方法也使用了`LogCombineHelper.print()`那么需要使用`LogCombineHelper.pushNest()`和`LogCombineHelper.popNest()`
   例子：

```java
class Test0 {
   @SneakyThrows
   public void test2() {
      LogCombineHelper.info("test:{},{}", 1, 2);
      LogCombineHelper.warn("test2:{},{}", 3, 4);
      //嵌套 手动调用
      LogCombineHelper.pushNest();
      sampleService.test3();
      LogCombineHelper.popNest();
      ExecutorService executorService = Executors.newFixedThreadPool(10);
      //support but you need to manual print
      executorService.execute(
              () -> {
                 LogCombineHelper.warn("test3:{}", 5);
                 try {
                    Thread.sleep(4000);
                 } catch (InterruptedException e) {
                    e.printStackTrace();
                 }
                 //这里出现嵌套，如果在方法前调用pushNest 方法后调用popNest的话，
                 // 会导致after nest print 这个日志割裂
                 LogCombineHelper.pushNest();
                 sampleService.test3();
                 LogCombineHelper.popNest();
                 LogCombineHelper.info("after nest print:{}", "after");
                 LogCombineHelper.print();
              }
      );
      LogCombineHelper.print();
   }
}
```

4. 【new】如果你想在异步线程中使用，请手动在异步线程的方法中调用 `LogCombineHelper.print()` 以实现异步线程的打印。

```java
class Test2 {

   @SneakyThrows
   public void test2() {
      LogCombineHelper.info("test:{},{}", 1, 2);
      LogCombineHelper.debug("test2:{},{}", 3, 4);
      new Thread(
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
      ).start();
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
    <version>0.0.2-SNAPSHOT</version>
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

5. 自定义日志格式
   支持类似与`Logback`的关键字 `%date` `%thread` `%level` `%class` `%line` `%msg` `pid`
   1. `%date` 支持自定义格式使用大括号括起来 ex: `年月日：%date{yyyy-MM-dd}`
   2. `%class`支持class自定义长度显示 ex: `类名：%class{25}`

默认打印格式为: `%date{yyyy-MM-dd HH:mm:ss,SSS} %level %pid --- [%thread]  %logger{35} - [%line] :%msg`  
`%date`的默认值为: `yyyy-MM-dd HH:mm:ss,SSS`   
`%class`的默认为 `35`