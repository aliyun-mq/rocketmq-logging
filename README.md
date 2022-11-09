# Logging for Apache RocketMQ

[![License][license-image]][license-url]
[![build][build-image]][build-url]
[![Maven Central][maven-image]][maven-url]

## Introduction

Inspired by [glowroot](https://github.com/glowroot/glowroot), this project aims to provide a shaded SLF4J/Logback,
making it possible to create an isolated environment for the logging system
of [Apache RocketMQ](https://rocketmq.apache.org/).

## Requirements

- Java 1.8 or later

## Getting Started

Add the following dependency to your project. Remember to replace `ROCKETMQ-LOGGING-VERSION` with
the [latest release](https://search.maven.org/search?q=g:io.github.aliyun-mq%20AND%20a:rocketmq-logging).

```xml
<dependencies>
    <dependency>
        <groupId>io.github.aliyun-mq</groupId>
        <artifactId>rocketmq-slf4j-api</artifactId>
        <version>ROCKETMQ-LOGGING-VERSION</version>
    </dependency>
    <dependency>
        <groupId>io.github.aliyun-mq</groupId>
        <artifactId>rocketmq-logback-classics</artifactId>
        <version>ROCKETMQ-LOGGING-VERSION</version>
    </dependency>
</dependencies>
```

This project uses a different configuration file, which is `rmq.logback.xml/rmq.logback-test.xml/rmq.logback.groovy`
instead of `logback.xml/logback-text.xml/logback.groovy`. Similar to standard logback configuration file, it will be
loaded from the classpath.

Besides, this library provides some extensions from logback, you can check them below.

* [io.github.aliyunmq.logback.extensions.CustomConsoleAppender](./rocketmq-logback-extensions/src/main/java/io/github/aliyunmq/logback/extensions/CustomConsoleAppender.java)
* [io.github.aliyunmq.logback.extensions.ProcessIdConverter](./rocketmq-logback-extensions/src/main/java/io/github/aliyunmq/logback/extensions/ProcessIdConverter.java)

<details><summary>A configuration example can be found here:
</summary>
<p>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <conversionRule conversionWord="pid" converterClass="io.github.aliyunmq.logback.extensions.ProcessIdConverter"/>
    <appender name="CustomConsoleAppender" class="io.github.aliyunmq.logback.extensions.CustomConsoleAppender">
        <encoder>
            <pattern>%yellow(%d{yyy-MM-dd HH:mm:ss.SSS,GMT+8}) %highlight(%-5p) %boldWhite([%pid]) %magenta([%t]) %boldGreen([%logger{12}#%M:%L]) - %m%n
            </pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>
    <appender name="DefaultAppender" class="org.apache.rocketmq.shade.ch.qos.logback.core.rolling.RollingFileAppender">
        <append>true</append>
        <File>
            ${rocketmq.log.root:-${user.home}${file.separator}logs${file.separator}rocketmq}${file.separator}rocketmq-client.log
        </File>
        <rollingPolicy class="org.apache.rocketmq.shade.ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <FileNamePattern>
                ${rocketmq.log.root:-${user.home}${file.separator}logs${file.separator}rocketmq}${file.separator}other_days${file.separator}rocketmq-client-%i.log.gz
            </FileNamePattern>
            <minIndex>1</minIndex>
            <maxIndex>${rocketmq.log.file.maxIndex:-10}</maxIndex>
        </rollingPolicy>
        <triggeringPolicy class="org.apache.rocketmq.shade.ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>64MB</maxFileSize>
        </triggeringPolicy>
        <encoder class="org.apache.rocketmq.shade.ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyy-MM-dd HH:mm:ss.SSS,GMT+8} %-5p [%t] [%logger{12}#%M:%L] - %m%n</pattern>
            <charset class="java.nio.charset.Charset">UTF-8</charset>
        </encoder>
    </appender>
    <root level="${rocketmq.log.level:-info}">
        <appender-ref ref="CustomConsoleAppender"/>
        <appender-ref ref="DefaultAppender" additivity="false"/>
    </root>
</configuration>
```

</p>
</details>

## Manual Release

Set the password in your `settings.xml` for repositories: `sonatype-nexus-snapshots-aliyun-mq`
and `sonatype-nexus-staging-aliyun-mq`, then execute the command below:

```bash
mvn clean install -Prelease
```

Sign in [nexus repository manager](https://s01.oss.sonatype.org/#stagingRepositories) and check the artifact, then determine whether to release it.

## Related Projects

* [rocketmq-slf4j-api-bridge](https://github.com/aliyun-mq/rocketmq-slf4j-api-bridge): Bridge for SLF4J API of Apache RocketMQ.

## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

[license-image]: https://img.shields.io/badge/license-Apache%202-4EB1BA.svg
[license-url]: https://www.apache.org/licenses/LICENSE-2.0.html
[build-image]: https://github.com/aliyun-mq/rocketmq-logging/actions/workflows/build.yml/badge.svg
[build-url]: https://github.com/aliyun-mq/rocketmq-logging/actions/workflows/build.yml
[maven-image]: https://maven-badges.herokuapp.com/maven-central/io.github.aliyun-mq/rocketmq-logging/badge.svg
[maven-url]: https://maven-badges.herokuapp.com/maven-central/io.github.aliyun-mq/rocketmq-logging
