# Parsing

在 MyBatis-3 中，`org.apache.ibatis.parsing` 包提供了用于解析和处理 MyBatis
配置文件中占位符表达式的功能。该包下的类主要用于解析和替换配置文件中的占位符，以便在运行时动态地生成最终的 SQL 语句。

下面是该包中一些重要类的解释：

- `GenericTokenParser`
  类：是一个通用的占位符解析器。它根据指定的开始和结束标记，解析并替换字符串中的占位符表达式。它使用 `TokenHandler`
  接口的实现来处理解析过程中的每个占位符。
- `ParsingException` 类：是解析过程中抛出的异常类，表示解析错误或不支持的语法。
- `PropertyParser` 类：是一个属性解析器，用于解析 MyBatis 配置文件中的占位符表达式。它将占位符替换为实际的属性值，可以从不同的配置源中获取属性值，例如配置文件、系统属性等。
- `TokenHandler` 接口：是一个回调接口，定义了处理占位符解析过程中每个占位符的方法。`GenericTokenParser`
  使用该接口的实现来处理解析过程中的占位符，例如进行属性值的替换、计算等操作。
- `XNode` 类：`XNode` 是 MyBatis 中用于表示 XML 节点的类。它是对 `org.w3c.dom.Node` 接口的封装，并提供了一些便捷方法用于获取和操作
  XML 节点的属性、文本内容和子节点。`XNode` 类通常用于解析 MyBatis 配置文件中的 XML 节点，方便处理和获取配置信息。
- `XPathParser` 类：`XPathParser` 是 MyBatis 中用于解析和操作 XML 的类。它基于 Java 的 `javax.xml.xpath.XPath` 接口，封装了
  XML 的解析和查询操作。`XPathParser` 可以加载 XML 文件或字符串，并提供了一些方法用于执行 XPath 表达式，从而方便地在 XML
  中查找和提取信息。
  - `XPathParser` 类的构造函数可以接受一个 `Document` 对象、`InputStream`、`Reader` 或 `String` 参数，用于加载 XML 内容。
  - `XPathParser` 类提供了 `evalXXX` 系列方法用于执行 XPath 表达式，并返回结果。例如，`evalNode` 方法返回匹配 XPath
    表达式的第一个节点的 `XNode` 对象，`evalNodes` 方法返回匹配 XPath 表达式的节点列表。

## GenericTokenParser

## PropertyParser

`PropertyParser` 是 MyBatis 框架中的一个工具类，用于解析配置文件中的属性占位符。它的主要作用是将包含占位符的字符串解析为实际的值。

在 MyBatis 的配置文件中，可以使用 `${property}` 的形式定义占位符。`PropertyParser` 类提供了静态方法 `parse`
，接收一个包含占位符的字符串，并尝试解析其中的占位符。

以下是 `PropertyParser` 类的核心方法 `parse` 的源码：

```java
public static String parse(String string,Properties variables){
        VariableTokenHandler handler=new VariableTokenHandler(variables);
        GenericTokenParser parser=new GenericTokenParser("${","}",handler);
        return parser.parse(string);
        }
```

方法中使用了 `VariableTokenHandler` 和 `GenericTokenParser` 两个类，它们是协同工作的关键组件。

`VariableTokenHandler` 是一个实现了 `TokenHandler` 接口的类，用于处理占位符中的变量。它接收一个 `Properties`
对象作为构造函数的参数，该对象存储了属性名与属性值的映射关系。在 `VariableTokenHandler` 的 `handleToken`
方法中，根据占位符中的属性名从 `Properties` 对象中获取对应的属性值。

`GenericTokenParser` 是一个通用的占位符解析器，它接收两个参数：开放标记符号和闭合标记符号。在 `parse` 方法中，使用 `${`
作为开放标记符号，`}` 作为闭合标记符号。`GenericTokenParser` 的 `parse`
方法会根据提供的标记符号解析字符串，并在解析过程中调用 `TokenHandler` 的 `handleToken` 方法处理占位符中的内容。

总结来说，`PropertyParser` 通过调用 `VariableTokenHandler` 和 `GenericTokenParser` 的方法，将包含占位符的字符串解析为实际的值。它在
MyBatis 的配置文件解析过程中起到了关键的作用，允许用户在配置文件中使用占位符引用属性值，提高了配置的灵活性和可复用性。
