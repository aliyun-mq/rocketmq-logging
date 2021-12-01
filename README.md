## Logging for Apache RocketMQ

### Getting started

Add slf4j and logback dependencies to `pom.xml`:
```xml
<dependency>
    <groupId>io.github.aliyun-mq</groupId>
    <artifactId>rocketmq-logback-shaded</artifactId>
    <version>${latest-version}</version>
</dependency>

<dependency>
    <groupId>io.github.aliyun-mq</groupId>
    <artifactId>rocketmq-slf4j-shaded</artifactId>
    <version>${latest-version}</version>
</dependency>
```

### Introduction

Current project is a shaded logback to replace the logging module of rocketmq.

To prevent the clash of logging configuration file with the [official logback](https://github.com/qos-ch/logback):

* `rocketmq-logback-shaded` using `rocketmq-logback.xml/rocketmq-logback-test.xml/rocketmq-logback.groovy` as its
  configuration file.

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
