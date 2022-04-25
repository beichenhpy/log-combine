# log-combine


NOW **NOT SUPPORT** ASYNC NON-BLOCKING THREAD

**WARNING**  
if you use `LogCombineHelper` in non-blocking thread like  `runnable`  
it maybe effects "ghost log", because a non-blocking thread method can not be controlled.


不支持非阻塞的多线程,如实现了Runnable的线程执行，使用后会导致日志混乱，出现脏日志。


## A tool for combined log printing in one method.

## how to use
you can see `log-combine-sample`, this is a simple example.
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
  
  class Test{
      @LogCombine
      public void test(){
          // do something
          LogCombineHelper.info("this is a test method, you can use it like {}", "logback");
      }
  }

```
