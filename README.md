## Logging for RocketMQ

Current project is a shaded logback to replace the logging module of rocketmq.

To prevent the clash of logging configuration file with the [official logback](https://github.com/qos-ch/logback):

* `rocketmq-logback-shaded` using `rocketmq-logback.xml/rocketmq-logback-test.xml/rocketmq-logback.groovy` as its
  configuration file.