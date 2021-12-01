## Logging for Apache RocketMQ

![Build Status](https://github.com/aliyun-mq/rocketmq-logging/actions/workflows/maven.yml/badge.svg)

### Introduction

Current project is a shaded logback to replace the logging module of rocketmq.

To prevent the clash of logging configuration file with the [official logback](https://github.com/qos-ch/logback):

* `rocketmq-logback-shaded` using `rocketmq-logback.xml/rocketmq-logback-test.xml/rocketmq-logback.groovy` as its
  configuration file.

Especially, the `extension` module:
1. provides a customized console appender which could be switched by envirment variable or system property: `mq.consoleAppender.enabled`.
2. provides a `ProcessIdConverter` which could print the id of current process.
### Getting started

Add slf4j and logback dependencies to `pom.xml`:
```xml
<dependency>
    <groupId>io.github.aliyun-mq</groupId>
    <artifactId>rocketmq-slf4j-shaded</artifactId>
    <version>${latest-version}</version>
</dependency>

<dependency>
    <groupId>io.github.aliyun-mq</groupId>
    <artifactId>rocketmq-logback-shaded</artifactId>
    <version>${latest-version}</version>
</dependency>
```

configuration example for `rocketmq.logback.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <conversionRule conversionWord="pid" converterClass="io.github.aliyunmq.logback.extensions.ProcessIdConverter"/>
    <appender name="CustomConsoleAppender" class="io.github.aliyunmq.logback.extensions.CustomConsoleAppender">
        <encoder>
            <pattern>%yellow(%d{yyy-MM-dd HH:mm:ss,GMT+8}) %highlight(%-5p) %boldWhite([%pid]) %magenta([%t]) %boldGreen([%logger{12}#%M:%L]) - %m%n
            </pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>
    <appender name="DefaultAppender" class="io.github.aliyunmq.rocketmq.shaded.ch.qos.logback.core.rolling.RollingFileAppender">
        <append>true</append>
        <File>
            ${rocketmq.log.root:-${user.home}${file.separator}logs${file.separator}rocketmq}${file.separator}mq-logback.log
        </File>
        <rollingPolicy class="io.github.aliyunmq.rocketmq.shaded.ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <FileNamePattern>
                ${rocketmq.log.root:-${user.home}${file.separator}logs${file.separator}rocketmq}${file.separator}other_days${file.separator}mq-logback-%i.log.gz
            </FileNamePattern>
            <minIndex>1</minIndex>
            <maxIndex>${rocketmq.log.file.maxIndex:-10}</maxIndex>
        </rollingPolicy>
        <triggeringPolicy class="io.github.aliyunmq.rocketmq.shaded.ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>64MB</maxFileSize>
        </triggeringPolicy>
        <encoder class="io.github.aliyunmq.rocketmq.shaded.ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyy-MM-dd HH:mm:ss,GMT+8} %-5p [%pid] [%t] [%logger{12}#%M:%L] - %m%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>
    <root level="${rocketmq.log.level:-info}">
        <appender-ref ref="CustomConsoleAppender"/>
        <appender-ref ref="DefaultAppender" additivity="false"/>
    </root>
</configuration>
```

### Requirements

<table>
  <tr>
    <td><b>Build required:</b></td>
    <td><b>Java 11 or later</b></td>
  </tr>
  <tr>
    <td><b>Runtime required:</b></td>
    <td><b>Java 6 or later</b></td>
  </tr>
</table>

### License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation
