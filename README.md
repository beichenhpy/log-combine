# log-combine

现在 **不支持非阻塞异步线程**

**⚠️警告**  
如果你使用了 `LogCombineHelper` 在一个非阻塞的线程中如实现了  `runnable` 接口的类 可能会导致【脏日志】， 因为一个异步非阻塞线程不可控。

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
