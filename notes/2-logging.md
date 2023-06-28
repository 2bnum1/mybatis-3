# logging 日志模块

日志模块选择一个日志框架, 用户可以配置, 否则按照Mybatis内置的顺序.
这个模块只需要看两个文件即可: `Log.java`, `LogFactory.java`

## logging目录是如何决定选择使用哪个日志框架的

在 MyBatis 中，选择使用哪个日志框架是通过以下几个步骤决定的：

1. MyBatis 首先检查类路径中是否存在常见的日志框架，例如 Log4j、Log4j2、Slf4j、JDK Logging 等。它会尝试通过检查这些日志框架的类是否存在来确定是否可用。
2. 如果多个日志框架都可用，MyBatis 将按照以下顺序选择一个日志框架：Slf4j、Commons Logging、Log4j2、Log4j、JDK Logging。Slf4j
   是首选框架，因为它提供了更好的兼容性和灵活性。
3. 一旦确定使用的日志框架，MyBatis 将使用 `LogFactory` 类动态创建对应的日志记录器实例。`LogFactory`
   类使用反射来实例化适配不同日志框架的日志记录器。
4. 在日志记录器实例化之后，MyBatis 将使用该日志记录器来记录运行时的日志信息。
   需要注意的是，MyBatis 通过检查类路径来确定可用的日志框架，并且它默认使用 Slf4j 作为首选框架。如果你希望使用其他的日志框架，可以将相应的日志框架的
   JAR 包添加到类路径中，并配置好对应的日志框架。这样，MyBatis 将会优先选择你配置的日志框架来记录日志信息。
   你可以在 MyBatis 的配置文件中使用 `<settings>` 元素的 `logImpl` 属性来显式指定要使用的日志框架，例如：

```xml

<settings>
    <setting name="logImpl" value="LOG4J2"/>
</settings>
```

> 该配置在Configuration中被设置

上述示例中，将会使用 Log4j2 作为日志框架。
总结来说，MyBatis 会根据类路径中存在的常见日志框架来选择一个可用的框架，并使用适配器模式将其与 MyBatis
的日志记录器进行适配，从而记录运行时的日志信息。

## 下一步

- [ ] 阅读`Log.java`
- [ ] 阅读`LogFactory.java`
