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
package org.apache.ibatis.builder;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import java.util.Properties;

@Intercepts({})
public class ExamplePlugin implements Interceptor {

  //实现拦截逻辑的地方，内部要通过invocation.proceed()显示的推进责任链前进
  //也就是调用下一个拦截器拦截目标方法
  public Object intercept(Invocation invocation) throws Throwable {
    return invocation.proceed();
  }

  //用当前拦截器生成目标对象的代理
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  //设置额外的参数，参数配置在拦截器的Properties节点里
  public void setProperties(Properties properties) {

  }

}
