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
