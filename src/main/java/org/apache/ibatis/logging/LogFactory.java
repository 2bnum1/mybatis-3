/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.logging;

import java.lang.reflect.Constructor;

/**
 * 给外部提供的方法: 根据类或者String获取Log对象
 * 内部实现逻辑: 根据一定的顺序设置选定的日志框架, 选择好一个后, 后面的跳过. 也可以由用户的自定义配置进行覆盖
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public final class LogFactory {

  /**
   * Marker Marker（标记）是一种用于标识和分类日志消息的机制, 对于那些支持Marker的日志框架, 使用 MYBATIS 作为标识
   * Marker to be used by logging implementations that support markers.
   */
  public static final String MARKER = "MYBATIS";

  /**
   * 作为静态变量缓存起来的 日志实现类构造器.
   * 用途: 1. 调用方调用getLog获取实现类的时候, LogFactory就使用这个构造器新建一个Log实现类
   * 2. 当这个变量被设置值之后, 就认为是选定了Log类, 后面的框架就不再选取了
   */
  private static Constructor<? extends Log> logConstructor;

  static {
    /*
     * 静态调用, 在程序初始化的时候就会执行.
     * 按顺序去设置各个日志框架, 相当于遍历所有支持的日志框架, 选择找到的第一个, 这也是Mybatis会按照推荐的顺序选择日志框架的逻辑
     */
    tryImplementation(LogFactory::useSlf4jLogging);
    tryImplementation(LogFactory::useCommonsLogging);
    tryImplementation(LogFactory::useLog4J2Logging);
    tryImplementation(LogFactory::useLog4JLogging);
    tryImplementation(LogFactory::useJdkLogging);
    tryImplementation(LogFactory::useNoLogging);
  }

  private LogFactory() {
    // 没用, 因为 LogFactory被设计为只对外提供静态方法, 所以不需要构造器
    // disable construction
  }

  public static Log getLog(Class<?> clazz) {
    // 调用方可以使用如下格式获取 Log 对象: Log log = LogFactory.getLog(this.class);
    return getLog(clazz.getName());
  }

  public static Log getLog(String logger) {
    try {
      // 使用被选定的框架构造器, 创建一个新的Log对象.
      return logConstructor.newInstance(logger);
    } catch (Throwable t) {
      throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
    }
  }

  /*
   * 这是用户可以配置使用什么框架的地方.
   * 程序先执行上方的 static {} 部分进行初始化, 再调用本方法进行覆盖设置, 即可实现用户定义的优先级更高.
   */
  public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
    setImplementation(clazz);
  }

  public static synchronized void useSlf4jLogging() {
    setImplementation(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
  }

  public static synchronized void useCommonsLogging() {
    setImplementation(org.apache.ibatis.logging.commons.JakartaCommonsLoggingImpl.class);
  }

  /**
   * @deprecated Since 3.5.9 - See https://github.com/mybatis/mybatis-3/issues/1223. This method will remove future.
   */
  @Deprecated
  public static synchronized void useLog4JLogging() {
    setImplementation(org.apache.ibatis.logging.log4j.Log4jImpl.class);
  }

  public static synchronized void useLog4J2Logging() {
    setImplementation(org.apache.ibatis.logging.log4j2.Log4j2Impl.class);
  }

  public static synchronized void useJdkLogging() {
    setImplementation(org.apache.ibatis.logging.jdk14.Jdk14LoggingImpl.class);
  }

  public static synchronized void useStdOutLogging() {
    setImplementation(org.apache.ibatis.logging.stdout.StdOutImpl.class);
  }

  public static synchronized void useNoLogging() {
    setImplementation(org.apache.ibatis.logging.nologging.NoLoggingImpl.class);
  }

  private static void tryImplementation(Runnable runnable) {
    // 如果之前已经选定了一个日志框架, 这里就不会执行了.
    if (logConstructor == null) {
      try {
        runnable.run();
      } catch (Throwable t) {
        // ignore
      }
    }
  }

  private static void setImplementation(Class<? extends Log> implClass) {
    try {
      // 使用反射, 获取类中, 只有一个参数为String, 的构造器
      Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
      // 使用获取到的构造器, 构造一个Log
      Log log = candidate.newInstance(LogFactory.class.getName());
      // 如果这个Log支持debug, 那么就记录一条日志
      if (log.isDebugEnabled()) {
        log.debug("Logging initialized using '" + implClass + "' adapter.");
      }
      logConstructor = candidate;
    } catch (Throwable t) {
      throw new LogException("Error setting Log implementation.  Cause: " + t, t);
    }
  }

}
