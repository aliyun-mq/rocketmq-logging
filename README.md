## Loging for RocketMQ

Current project is a shaded logback to replace the logging module of rocketmq.

To prevent the clash of logging configuration file with the [official logback](https://github.com/qos-ch/logback):

* `rocketmq-logback-shaded` using `rocketmq-logback.xml/rocketmq-logback-test.xml/rocketmq-logback.groovy` as its
  configuration file.
* `rocketmq-wrapper-logback-shaded`
  using `rocketmq-wrapper-logback.xml/rocketmq-wrapper-logback-test.xml/rocketmq-wrapper-logback.groovy` as its
  configuration file.
