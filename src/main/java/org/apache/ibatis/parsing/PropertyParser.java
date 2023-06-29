/*
 *    Copyright 2009-2023 the original author or authors.
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
package org.apache.ibatis.parsing;

import java.util.Properties;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class PropertyParser {

  private static final String KEY_PREFIX = "org.apache.ibatis.parsing.PropertyParser.";
  /**
   * 这个配置表示是否支持给Property设置默认值, 默认为false.
   * 当设置为true的时候, 支持解析类似于${db.username:postgres}的Property, 也就是说当Property中没有这个配置项的时候, 取冒号后面的默认值.
   * 当设置为false的时候, 如果没有提供 db.username 的属性值, 占位符将保持 ${db.username:postgres} 的形式.
   * The special property key that indicate whether enable a default value on placeholder.
   * <p>
   * The default value is {@code false} (indicate disable a default value on placeholder) If you specify the
   * {@code true}, you can specify key and default value on placeholder (e.g. {@code ${db.username:postgres}}).
   * </p>
   *
   * @since 3.4.2
   */
  public static final String KEY_ENABLE_DEFAULT_VALUE = KEY_PREFIX + "enable-default-value";

  /**
   * 配置项的key和默认值之间的分隔符, 默认为:
   * 如果设置为其他值, 例如? 那么带有默认值的配置项需要改为 ${db.username?postgres}
   * The special property key that specify a separator for key and default value on placeholder.
   * <p>
   * The default separator is {@code ":"}.
   * </p>
   *
   * @since 3.4.2
   */
  public static final String KEY_DEFAULT_VALUE_SEPARATOR = KEY_PREFIX + "default-value-separator";

  private static final String ENABLE_DEFAULT_VALUE = "false";
  private static final String DEFAULT_VALUE_SEPARATOR = ":";

  private PropertyParser() {
    // Prevent Instantiation
  }

  public static String parse(String string, Properties variables) {
    // 核心代码: 使用`VariableTokenHandler`作为token处理器, 使用`GenericTokenParser`作为解析器
    // 先使用GenericTokenParser解析出${TOKEN}格式的token, 使用VariableTokenHandler决定如何处理解析出来的token
    VariableTokenHandler handler = new VariableTokenHandler(variables);
    GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
    return parser.parse(string);
  }

  private static class VariableTokenHandler implements TokenHandler {
    private final Properties variables;
    private final boolean enableDefaultValue;
    private final String defaultValueSeparator;

    private VariableTokenHandler(Properties variables) {
      // 构造器接收一个Properties, 并从中解析出前面的两个参数
      this.variables = variables;
      this.enableDefaultValue = Boolean.parseBoolean(getPropertyValue(KEY_ENABLE_DEFAULT_VALUE, ENABLE_DEFAULT_VALUE));
      this.defaultValueSeparator = getPropertyValue(KEY_DEFAULT_VALUE_SEPARATOR, DEFAULT_VALUE_SEPARATOR);
    }

    private String getPropertyValue(String key, String defaultValue) {
      // 对Properties.getProperty的封装, 兼容properties是null的情况
      return variables == null ? defaultValue : variables.getProperty(key, defaultValue);
    }

    /*
     * 当Properties: {"db.username": "postgres"}
     * 需要解析${db.username}, 不支持默认值或不支持默认值 => postgres
     * 需要解析${db.password:123456}, 不支持默认值 => ${db.password:123456}. 支持默认值 => 123456
     */
    @Override
    public String handleToken(String content) {
      // 首先，检查 variables 是否为 null，即是否存在属性值的映射关系。
      if (variables != null) {
        String key = content;
        // 如果 enableDefaultValue 为 true，表示允许使用默认值
        if (enableDefaultValue) {
          // 在占位符内容 content 中查找默认值分隔符 defaultValueSeparator 的索引。
          final int separatorIndex = content.indexOf(defaultValueSeparator);
          String defaultValue = null;
          if (separatorIndex >= 0) {
            // 如果找到了默认值分隔符，则将分隔符之前的部分作为属性名 key，将分隔符之后的部分作为默认值 defaultValue。
            key = content.substring(0, separatorIndex);
            defaultValue = content.substring(separatorIndex + defaultValueSeparator.length());
          }
          // 如果默认值 defaultValue 不为 null，则尝试从 variables 中获取属性名 key 对应的属性值。如果属性值存在，则返回该值；否则，返回默认值 defaultValue。
          if (defaultValue != null) {
            return variables.getProperty(key, defaultValue);
          }
        }
        // 如果 enableDefaultValue 为 false 或者未找到默认值分隔符，则将整个占位符内容 content 作为属性名 key。
        // 检查 variables 中是否包含属性名 key，如果存在，则返回对应的属性值。
        if (variables.containsKey(key)) {
          return variables.getProperty(key);
        }
      }
      // 如果以上步骤都没有返回属性值，则将原始的占位符内容 content 加上 ${}，表示解析失败，返回未解析的占位符。
      return "${" + content + "}";
    }
  }

}
