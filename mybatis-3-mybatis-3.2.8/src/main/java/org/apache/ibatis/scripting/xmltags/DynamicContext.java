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
package org.apache.ibatis.scripting.xmltags;

import java.util.HashMap;
import java.util.Map;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;

/**
 * Mybatis中参数传递和使用过程：
 * 将传入的参数对象统一封装为ContextMap对象（继承了HashMap对象），然后OGNL运行时
 * 环境在动态计算sql语句时，会按照ContextAccessor中描述的Map接口的方式来访问和读取
 * ContextMap对象，获取计算过程中需要的参数。ContextMap对象内部可能封装了一个普通的POJO对象
 * 也可以是直接传递的Map对象，当然从外部是看不出来的，因为都是使用Map的接口来读取数据
 * @author Clinton Begin
 */
/**
 * 在DynamicContext类中，有对传入的parameterObject对象进行"map"化处理的部分
 * 也就是说，你传入的pojo对象，会被当做一个键值对来进行处理
 * 读取这个pojo对象的接口，还是Map对象
 * @author donson
 *
 */
public class DynamicContext {

  public static final String PARAMETER_OBJECT_KEY = "_parameter";
  public static final String DATABASE_ID_KEY = "_databaseId";

  static {
	//使用OGNL表达式
    OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
  }

  private final ContextMap bindings;
  private final StringBuilder sqlBuilder = new StringBuilder();
  private int uniqueNumber = 0;

  /**
   * 根据传入的参数对象是否为Map类型，有两种不同构造ContextMap的方式。
   * 而ContextMap作为一个继承了HashMap的对象，作用就是用于统一参数的访问方式：用Map接口方法来访问数据
   * 具体来说，当传入的参数对象不是Map类型时，Mybatis会将传入的POJO对象用MetaObject对象来封装，当动态计算
   * sql过程需要获取数据时，用Map接口的get方法包装MetaObject对象的取值过程。
   * @param configuration
   * @param parameterObject
   */
  public DynamicContext(Configuration configuration, Object parameterObject) {
    if (parameterObject != null && !(parameterObject instanceof Map)) {
      MetaObject metaObject = configuration.newMetaObject(parameterObject);
      bindings = new ContextMap(metaObject);
    } else {
      bindings = new ContextMap(null);
    }
    bindings.put(PARAMETER_OBJECT_KEY, parameterObject);
    bindings.put(DATABASE_ID_KEY, configuration.getDatabaseId());
  }

  public Map<String, Object> getBindings() {
    return bindings;
  }

  public void bind(String name, Object value) {
    bindings.put(name, value);
  }

  public void appendSql(String sql) {
    sqlBuilder.append(sql);
    sqlBuilder.append(" ");
  }

  public String getSql() {
    return sqlBuilder.toString().trim();
  }

  public int getUniqueNumber() {
    return uniqueNumber++;
  }

  static class ContextMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 2977601501966151582L;

    private MetaObject parameterMetaObject;
    public ContextMap(MetaObject parameterMetaObject) {
      this.parameterMetaObject = parameterMetaObject;
    }

    @Override
    public Object put(String key, Object value) {
      return super.put(key, value);
    }
    
    @Override
    public Object get(Object key) {
      String strKey = (String) key;
      if (super.containsKey(strKey)) {
        return super.get(strKey);
      }

      if (parameterMetaObject != null) {
        Object object = parameterMetaObject.getValue(strKey);
        // issue #61 do not modify the context when reading
//        if (object != null) { 
//          super.put(strKey, object);
//        }

        return object;
      }

      return null;
    }
  }

  /**
   * ContextAccessor是DynamicContext的内部类，实现了OGNL中的PropertyAccessor接口，为OGNL提供了
   * 如何使用ContextMap参数对象的说明
   * @author donson
   *
   */
  static class ContextAccessor implements PropertyAccessor {

    public Object getProperty(Map context, Object target, Object name)
        throws OgnlException {
      Map map = (Map) target;

      Object result = map.get(name);
      if (result != null) {
        return result;
      }

      Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
      if (parameterObject instanceof Map) {
    	  return ((Map)parameterObject).get(name);
      }

      return null;
    }

    public void setProperty(Map context, Object target, Object name, Object value)
        throws OgnlException {
      Map map = (Map) target;
      map.put(name, value);
    }
  }
}