/*
 *    Copyright 2009-2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.session;

import java.sql.Connection;

/**
 * @author Clinton Begin
 */
/**
 * 实际上，这个声明定义的类型是一个类，它有5个实例
 * 在比较两个枚举类型的值时，永远不需要调用equals，而直接使用"=="就可以了
 * 如果需要的话，可以在枚举类型中添加一些构造器、方法和域，当然，构造器只是在构造枚举常量时被调用
 * @author donson
 *
 */
public enum TransactionIsolationLevel {
  NONE(Connection.TRANSACTION_NONE),
  READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED), //提交读   2
  READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),//未提交读 1
  REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ), //可重复读 3
  SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);   //可串行化 4

  private final int level;

  //构造器只是在构造枚举常量时被调用
  private TransactionIsolationLevel(int level) {
    this.level = level;
  }

  public int getLevel() {
    return level;
  }
}
