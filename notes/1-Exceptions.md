# Exception异常

`org.apache.ibatis.exceptions` 目录下的类提供了 MyBatis 运行时可能出现的异常相关的实现。下面是该目录中一些重要类的简要说明：

- `PersistenceException`：是 MyBatis 异常的基类，所有 MyBatis 异常类都直接或间接继承自该类。它扩展自 `RuntimeException`
  ，用于表示 MyBatis 运行时异常。

- `IbatisException`：是 `PersistenceException` 的旧版本别名，保留了向后兼容性。

- `BuilderException`：继承自 `PersistenceException`，表示 MyBatis 构建器过程中的异常，例如解析配置文件、构建对象时出现的错误。

- `CachingException`：继承自 `PersistenceException`，表示 MyBatis 缓存相关的异常，例如缓存实例化、缓存命中时出现的错误。

- `DataAccessDeniedException`：继承自 `PersistenceException`，表示 MyBatis 数据访问被拒绝的异常，例如数据库连接权限不足或操作权限不正确。

- `DataRetrievalFailureException`：继承自 `PersistenceException`，表示 MyBatis 数据检索失败的异常，例如查询结果为空或获取数据失败。

- `NestedSQLException`：继承自 `PersistenceException`，表示 MyBatis 内部 SQL 执行时出现的异常，例如 SQL 语句执行错误、数据库连接断开等。

- `TimeoutException`：继承自 `PersistenceException`，表示 MyBatis 操作超时的异常，例如等待数据库连接超时、等待锁超时等。

- `TooManyResultsException`：继承自 `PersistenceException`，表示 MyBatis 查询结果过多的异常，通常在期望返回单一结果但实际返回多个结果时抛出。

这些异常类提供了对 MyBatis 运行时可能出现的各种异常情况进行了分类和封装。通过捕获这些异常，可以更好地了解在 MyBatis
的运行过程中可能出现的问题，并根据具体情况进行异常处理和错误处理。
