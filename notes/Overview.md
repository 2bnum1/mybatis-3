# 1. 目录结构

MyBatis 源代码的 `src` 文件夹下的各个一级目录的含义如下：

1. `main` 目录：
   - `java` 目录：包含了 MyBatis 的核心 Java 代码，包括解析器、执行器、映射器等。
   - `resources` 目录：包含了 MyBatis 的配置文件和一些资源文件，例如日志配置、XML 映射文件等。

2. `test` 目录：
   - `java` 目录：包含了 MyBatis 的测试代码。
   - `resources` 目录：包含了 MyBatis 测试所需的资源文件。

3. `site` 目录：
   - 包含了 MyBatis 官方网站的相关文件，例如文档、示例代码等。

下面是 `main/java` 目录下一些重要的子目录及其含义：

- `org.apache.ibatis.annotations`：包含了 MyBatis 注解相关的接口，用于定义映射文件中的 SQL 语句。
- `org.apache.ibatis.binding`：包含了 MyBatis 绑定相关的类，用于将接口和映射器文件进行绑定，生成代理对象。
- `org.apache.ibatis.builder`：包含了 MyBatis 构建器相关的类，用于构建配置对象、语句对象等。
- `org.apache.ibatis.cursor`：包含了 MyBatis 游标相关的类，用于处理大数据量的查询结果。
- `org.apache.ibatis.exceptions`：包含了 MyBatis 异常相关的类，用于处理 MyBatis 运行时发生的异常。
- `org.apache.ibatis.lang`：包含了 MyBatis 的一些通用工具类。
- `org.apache.ibatis.logging`：包含了 MyBatis 日志相关的类，用于记录 MyBatis 运行时的日志信息。
- `org.apache.ibatis.parsing`：包含了 MyBatis 解析相关的类，用于解析和处理 XML 配置文件和 SQL 语句。
- `org.apache.ibatis.scripting`：包含了 MyBatis 脚本相关的类，用于解析和执行动态 SQL 语句。
- `org.apache.ibatis.util`：包含了 MyBatis 的一些通用工具类。
- `org.apache.ibatis.builder`：包含了 MyBatis 配置解析器和构建器相关的类，负责将配置文件解析成相应的对象模型。
- `org.apache.ibatis.cache`：包含了 MyBatis 缓存相关的类，用于提高查询性能。
- `org.apache.ibatis.datasource`：包含了 MyBatis 数据源相关的类，用于与数据库建立连接。
- `org.apache.ibatis.executor`：包含了 MyBatis 执行器相关的类，用于执行 SQL 语句。
- `org.apache.ibatis.io`：包含了 MyBatis IO 相关的类，用于处理资源文件的加载和读取。
- `org.apache.ibatis.jdbc`：包含了 MyBatis JDBC 相关的类，用于与数据库进行交互。
- `org.apache.ibatis.mapping`：包含了 MyBatis 映射相关的类，用于将 Java 对象与数据库表之间进行映射。
- `org.apache.ibatis.plugin`：包含了 MyBatis 插件相关的类，用于扩展 MyBatis 的功能。
- `org.apache.ibatis.reflection`：包含了 MyBatis 反射相关的类，用于处理对象的属性和方法等。
- `org.apache.ibatis.scripting`：包含了 MyBatis 脚本相关的类，用于解析和执行动态 SQL 语句。
- `org.apache.ibatis.session`：包含了 MyBatis 会话相关的类，用于管理数据库会话和事务。
- `org.apache.ibatis.transaction`：包含了 MyBatis 事务相关的类，用于管理数据库事务。
- `org.apache.ibatis.type`：包含了 MyBatis 类型处理器相关的类，用于处理 Java 对象与数据库字段之间的转换。

这些目录和包结构组织了 MyBatis 源代码的各个模块，每个模块负责不同的功能，共同构成了 MyBatis 框架的核心部分。

# 2. 阅读顺序

理解 MyBatis 源代码的最佳顺序可以按照以下方式来阅读 `main/java` 目录下的子目录：

- [ ] `org.apache.ibatis.exceptions`：开始阅读异常相关的类，以了解 MyBatis 运行时可能出现的异常情况和异常处理机制。
- [ ] `org.apache.ibatis.logging`：接着阅读日志相关的类，了解 MyBatis 如何记录运行时的日志信息，以及日志记录器的配置和实现。
- [ ] `org.apache.ibatis.lang` 和 `org.apache.ibatis.util`：这些通用工具类可以在阅读其他模块时使用，因此可以提前了解它们，以便更好地理解其他代码的实现。
- [ ] `org.apache.ibatis.parsing`：阅读解析相关的类，了解 MyBatis 如何解析 XML 配置文件和 SQL 语句，以及解析过程中涉及的关键逻辑和数据结构。
- [ ] `org.apache.ibatis.builder`：接下来阅读构建器相关的类，了解 MyBatis 如何构建配置对象、语句对象等，以及构建过程中涉及的关键逻辑和数据结构。
- [ ] `org.apache.ibatis.scripting`：继续阅读脚本相关的类，了解 MyBatis 如何解析和执行动态 SQL 语句，以及动态 SQL 的实现原理和机制。
- [ ] `org.apache.ibatis.binding`：阅读绑定相关的类，了解 MyBatis 如何将接口和映射器文件进行绑定，生成代理对象，并实现接口的方法。
- [ ] `org.apache.ibatis.cursor`：继续阅读游标相关的类，了解 MyBatis 如何处理大数据量的查询结果，以及游标的使用和优化。
- [ ] `org.apache.ibatis.session`：阅读会话相关的类，了解 MyBatis 的会话管理和事务处理机制，以及会话的生命周期和作用域。
- [ ] `org.apache.ibatis.executor`：继续阅读执行器相关的类，了解 MyBatis 如何执行 SQL 语句，包括查询和更新操作，以及执行过程中的缓存处理和结果映射等。
- [ ] `org.apache.ibatis.mapping`：继续阅读映射相关的类，了解 MyBatis 如何将 Java 对象和数据库表进行映射，包括参数映射和结果映射的实现。
- [ ] `org.apache.ibatis.datasource` 和 `org.apache.ibatis.transaction`：最后阅读数据源和事务相关的类，了解 MyBatis
  如何管理数据库连接和事务的获取、提交和回滚。

这个阅读顺序可以帮助你逐步理解 MyBatis 的核心模块和功能，并按照逻辑顺序阅读代码，逐步揭示 MyBatis
的实现细节和设计思路。当然，根据你的实际需求和兴趣，你也可以选择自己感兴趣的模块先进行深入阅读。
