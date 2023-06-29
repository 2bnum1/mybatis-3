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

/**
 * @author Clinton Begin
 */
public class GenericTokenParser {

  private final String openToken;
  private final String closeToken;
  private final TokenHandler handler;

  /*
   * 指定开始符号和结束符号, 并使用 TokenHandler 这个接口的实现类进行解析
   * 例如 开始符号为 #{ 结束符号为 }, 就会调用handler处理 #{待解析的内容}
   */
  public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
    this.openToken = openToken;
    this.closeToken = closeToken;
    this.handler = handler;
  }

  /*
    这段代码通过搜索开放标记符号和闭合标记符号之间的内容，并使用 `handler` 对解析出的表达式进行处理。下面是代码的执行流程：
    1. 如果传入的 `text` 为 `null` 或空字符串，直接返回空字符串。
    2. 在 `text` 中搜索开放标记符号（如 `${`）的索引，如果找不到则直接返回 `text`。
    3. 将 `text` 转换为字符数组 `src`，并初始化偏移量 `offset` 为 0。
    4. 创建一个 `StringBuilder` 对象 `builder` 用于构建最终的解析结果。
    5. 创建一个 `StringBuilder` 对象 `expression` 用于存储解析出的表达式内容。
    6. 进入循环，在循环中执行以下操作：
    - 如果当前的开放标记符号前面的字符是反斜杠 `\`，表示该开放标记符号被转义了，移除反斜杠并将开放标记符号追加到 `builder` 中，更新偏移量 `offset`。
    - 否则，表示找到了开放标记符号，开始搜索闭合标记符号。
    - 如果 `expression` 为 `null`，表示开始解析新的表达式，重置 `expression` 的内容；否则，清空 `expression` 的内容。
    - 将开放标记符号前的文本追加到 `builder` 中。
    - 更新偏移量 `offset`。
    - 在 `text` 中从偏移量 `offset` 开始搜索闭合标记符号。
    - 进入循环，直到找到闭合标记符号或结束符号为止：
    - 如果闭合标记符号前面的字符不是反斜杠，表示找到了闭合标记符号，将闭合标记符号前的内容追加到 `expression` 中，并跳出循环。
    - 否则，表示闭合标记符号被转义了，移除反斜杠并将闭合标记符号追加到 `expression` 中，更新偏移量 `offset`。
    - 继续在 `text` 中从偏移量 `offset` 开始搜索闭合标记符号。
    - 如果未找到闭合标记符号，表示解析失败，将开放标记符号后的文本追加到 `builder` 中，并更新偏移量 `offset`。
    - 否则，将通过 `handler` 对 `expression.toString()` 进行处理，并将处理结果追加到 `builder` 中，更新偏移量 `offset`。
    - 更新开始搜索开放标记符号的索引 `start`。
    7. 如果 `offset` 小于字符数组 `src` 的长度，表示还有剩余的文本未处理，将剩余的文本追加到 `builder` 中。
    8. 返回 `builder.toString()`，即最终的解析结果。

    该方法的作用是解析包含占位符的文本，将占位符替换为相应的值。例如，`${name}` 表示一个占位符，解析时会将其替换为 `name` 对应的值。
  */
  public String parse(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    // 搜索开放标记符号
    int start = text.indexOf(openToken);
    if (start == -1) {
      return text;
    }
    char[] src = text.toCharArray();
    int offset = 0;
    final StringBuilder builder = new StringBuilder();
    StringBuilder expression = null;
    do {
      if (start > 0 && src[start - 1] == '\\') {
        // 该开放标记符号被转义了，移除反斜杠并继续
        builder.append(src, offset, start - offset - 1).append(openToken);
        offset = start + openToken.length();
      } else {
        // 找到开放标记符号，开始搜索闭合标记符号
        if (expression == null) {
          expression = new StringBuilder();
        } else {
          expression.setLength(0);
        }
        builder.append(src, offset, start - offset);
        offset = start + openToken.length();
        int end = text.indexOf(closeToken, offset);
        while (end > -1) {
          if ((end <= offset) || (src[end - 1] != '\\')) {
            expression.append(src, offset, end - offset);
            break;
          }
          // 该闭合标记符号被转义了，移除反斜杠并继续
          expression.append(src, offset, end - offset - 1).append(closeToken);
          offset = end + closeToken.length();
          end = text.indexOf(closeToken, offset);
        }
        if (end == -1) {
          // 未找到闭合标记符号
          builder.append(src, start, src.length - start);
          offset = src.length;
        } else {
          builder.append(handler.handleToken(expression.toString()));
          offset = end + closeToken.length();
        }
      }
      start = text.indexOf(openToken, offset);
    } while (start > -1);
    if (offset < src.length) {
      builder.append(src, offset, src.length - offset);
    }
    return builder.toString();
  }
}
