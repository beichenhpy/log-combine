# log-combine
for combine log printing

**NOW NOT SUPPORT ASYNC NON-BLOCKING THREAD**

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
  
  @LogCombine
  public void test(){
    // do something
    LogCombineHelper.info("this is a test method, you can use it like {}", "logback");
  }

```
