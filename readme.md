# Java商场秒杀系统设计与实战

来源：基于debug在B站上分享的视频学习

## 环境准备

具体环境配置自行百度

1.IDEA/Navicat/JDK1.8

2.MAVEN(注意配置maven镜像和本地仓库地址，maven安装目录->conf->settings.xml)

```
<mirror>
     <id>alimaven</id>
     <mirrorOf>central</mirrorOf>
     <name>aliyun maven</name>
     <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
 </mirror>
 <mirror>
     <id>alimaven</id>
     <name>aliyun maven</name>
     <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
     <mirrorOf>central</mirrorOf>
 </mirror>
 <mirror>
     <id>central</id>
     <name>Maven Repository Switchboard</name>
     <url>http://repo1.maven.org/maven2/</url>
     <mirrorOf>central</mirrorOf>
 </mirror>
 <mirror>
     <id>repo2</id>
     <mirrorOf>central</mirrorOf>
     <name>Human Readable Name for this Mirror.</name>
     <url>http://repo2.maven.org/maven2/</url>
 </mirror>
 <mirror>
     <id>ibiblio</id>
     <mirrorOf>central</mirrorOf>
     <name>Human Readable Name for this Mirror.</name>
     <url>http://mirrors.ibiblio.org/pub/mirrors/maven2/</url>
 </mirror>
 <mirror>
     <id>jboss-public-repository-group</id>
     <mirrorOf>central</mirrorOf>
     <name>JBoss Public Repository Group</name>
     <url>http://repository.jboss.org/nexus/content/groups/public</url>
 </mirror>
 <mirror>
     <id>google-maven-central</id>
     <name>Google Maven Central</name>
     <url>https://maven-central.storage.googleapis.com
     </url>
     <mirrorOf>central</mirrorOf>
 </mirror>
 <!-- 中央仓库在中国的镜像 -->
 <mirror>
     <id>maven.net.cn</id>
     <name>oneof the central mirrors in china</name>
     <url>http://maven.net.cn/content/groups/public/</url>
     <mirrorOf>central</mirrorOf>
 </mirror>
```

需IDEA配置MAVEN

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613145309370.png" alt="image-20200613145309370" style="zoom:50%;" />

3.MySql:5.7.30

4.Zookeeper-3.4.6

5.Tomcat

IDEA配置Tomcat

![image-20200613153142174](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613153142174.png)

6.Redis

7.RabbitMQ（参考我之前的博客：）

## 业务流程

![image-20200613150126519](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613150126519.png)

![image-20200613165205615](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613165205615.png)

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613144220390.png" alt="image-20200613144220390" /><img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613144220390.png" alt="image-20200613144220390" style="zoom:50%;" />

学习目标：

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613144408891.png" alt="image-20200613144408891" style="zoom:50%;" />

![image-20200613144108095](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613144108095.png)

## MVC开发流程

![image-20200613144132052](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613144132052.png)

### 简单的一个CV示例（页面跳转）：

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613152949261.png" alt="image-20200613152949261" style="zoom:33%;" />

controller:

往前端传数据

```
package com.debug.kill.server.controller;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础controller
 * @Author nilzxq
 * @Date 2020-06-13 15:06
 */
@Controller
@RequestMapping("base")
public class BaseController {
    private static final Logger log=LoggerFactory.getLogger(BaseController.class);

    //http://localhost:8092/kill/base/welcome
    @GetMapping("/welcome")
    public String welcome(String name, ModelMap modelMap){
        if(StringUtils.isBlank(name)){
            name="这是welcome";
        }
        modelMap.put("name",name);
        return "welcome";
    }
}

```

view

:zap:注意加载:`<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>`

否则前端无法解析后端的数据

welcome.jsp

```
<%--
  Created by IntelliJ IDEA.
  User: steadyjack
  Date: 2019/5/20
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
    <h1>这是欢迎页面</h1>
    <br/>
    ${name}
    <%--<c:out value="${name}"></c:out>--%>
</body>
</html>
```

:zap:注意设置断点，查看控制台输出的Mapping路径

![image-20200613154049731](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613154049731.png)

![image-20200613153927222](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613153927222.png)



![image-20200613154353652](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613154353652.png)

```
// @ResponseBody数据以json的格式异步传递给前端,不跳转页面，直接拿后端数据到前端
@RequestMapping(value = "/data",method = RequestMethod.GET)
@ResponseBody
public String data(String name){
    if(StringUtils.isBlank(name)){
        name="这是welcome";
    }
    return name;
}
```

如下图所示：/data没有跳转页面，/welcome跳转到了首页

从代码上来看，区别在于不跳转页面加了@ResponseBody且return返回的是参数，而不是页面。

![image-20200613155342187](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613155342187.png)c

### 封装状态码



![image-20200613155946115](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613155946115.png)

```
package com.debug.kill.api.enums;

/**
 * 通用状态码
 */
public enum StatusCode {

    Success(0,"成功"),
    Fail(-1,"失败"),
    InvalidParams(201,"非法的参数!"),
    UserNotLogin(202,"用户没登录"),

    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

```

![image-20200613161948825](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613161948825.png)

```
	@RequestMapping(value = "/response",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse response(String name){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        if(StringUtils.isBlank(name)){
            name="这是welcome";
        }
        response.setData(name);
        return response;
    }
```



![image-20200613161656553](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613161656553.png)

安装FeHelper插件后显示效果：

![image-20200613164519926](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613164519926.png)

### [@responseBody注解的使用](https://www.cnblogs.com/qiankun-site/p/5774325.html)

1、

　　@responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML

　　数据，需要注意的呢，在使用此注解之后不会再走试图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。

2、　　

　　@RequestMapping("/login")
　　@ResponseBody
　　public User login(User user){
　　　　return user;
　　}
　　User字段：userName pwd
　　那么在前台接收到的数据为：'{"userName":"xxx","pwd":"xxx"}'

　　效果等同于如下代码：
　　@RequestMapping("/login")
　　public void login(User user, HttpServletResponse response){
　　　　response.getWriter.write(JSONObject.fromObject(user).toString());
　　}

## MyBatis 逆向工程

https://github.com/enamor/ssm

## 设计与实战

![image-20200613175518252](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613175518252.png)

从该目录结构中可以看出，该项目为一个“聚合型项目”，其中，model模块依赖api模块，server模块依赖model模块，层层依赖！最终在server模块实现“大汇总”，即server模块为整个项目的核心关键所在，像什么“配置文件”、“入口启动类”啥的都在这个模块中！

```
select 
a.*, 
b.name as itemName,
(
	case when (now() BETWEEN a.start_time and a.end_time and a.total>0)
	 then 1
	ELSE 0
	END
)as canKill
 from item_kill as a left JOIN item AS b on b.id=a.item_id WHERE a.is_active=1

```

![image-20200613183929073](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200613183929073.png)

controller调用service,service调用dao,dao调用mapper

### Shiro实现用户登录认证

对于Shiro，相信各位小伙伴应该听说过，甚至应该也使用过！简单而言，它是一个很好用的用户身份认证、权限授权框架，可以实现用户登录认证，权限、资源授权、会话管理等功能，在本秒杀系统中，我们将主要采用该框架实现对用户身份的认证和用户的登录功能

![image-20200616163515277](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616163515277.png)

![image-20200616165913191](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616165913191.png)



 ### 订单编号的生成方式

1. 采用传统的时间戳+N为流水号（UUID也是一个道理）构成

2. 雪花算法：高效率生成分布式唯一ID的最佳方式 https://github.com/souyunku/SnowFlake

   比较一：传统的方式要么太长，没法排序；要么需要依赖中间件。 
   比较二：雪花算法-（整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞，高效率

### 整合前端实现完整的秒杀逻辑

建议：站在用户角度上使用自己的系统；Take User As Fool！！ 



### 整合RabbitMQ实现消息异步发送 

步骤一：加入RabbitMQ的依赖、配置RabbitMQ服务的相关信息 
步骤二：自定义注入RabbitMQ的相关配置-RabbitmqConfig 
步骤三：创建基本的消息模型，实现消息的发送和接收 

进入sbin目录：

`rabbitmq-plugins enable rabbitmq_management`

http://localhost:15672

312错误

### 邮件服务发送通知信息实战

步骤一：加入邮件依赖、整合邮件配置信息

步骤二：封装统一发送邮件的服务

步骤三：实现多种发送邮件的方式



### 整体再次回顾秒杀的全过程

技能一：业务流程的分析与业务模块的划分

技能二：业务模块的服务化以及功能模块的解耦

### 死信队列失效超时未支付的订单



![image-20200615193133183](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200615193133183.png)

RabbitMQ死信队列缺陷：有许多订单在某个TTL集中失效，但是恰好RabbitMQ服务挂掉了

补充解决方案：基于@Scheduled注解的定时任务实现-批量获取status=0的订单并判断时间超过了TTL

完整解决方案：结合线程池一起使用，规避单一线程处理多个任务调度的缺陷，支持多线程处理

### 查看订单详情

步骤：根据订单编号orderNo直接获取订单详情！

### Jmeter高并发压力测试

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200615213504475.png" alt="image-20200615213504475" style="zoom:50%;" />

### 问题分析

致命点：产生的多个线程对“同一段操作共享数据的代码”进行并发操作，从而出现并发安全的问题！

核心方案：分布式锁解决共享资源在高并发访问时出现的“并发安全”问题

协助方案：对于瞬时流量、并发请求进行限流（目前是接口的限流，有条件时还能进行网关层面的限流）

辅助方案一：应用（秒杀系统）、中间件（RabbitMQ Redis......）服务做集群部署，提高高可用与稳定性！

辅助方案二：数据库MySql做主备部署，如可以搭建一个Master写库，多个Slave读库实例的服务！

### 秒杀逻辑优化

#### 数据库MySql层面优化抢单逻辑

核心SQL逻辑："查询以及更减库存"时需要判断当前“可被更减的数量”是否仍然还大于0

#### 基于Redis的分布式锁优化抢单逻辑

核心方法：SETNX+EXPIRE联合使用

原因：Redis本身就是一个基于内存的，单线程的Key-Value存储数据库

#### 基于Redisson的分布式锁优化抢单逻辑

Redisson是一个在Redis的基础上实现的Java驻内存数据网格（In-Memory Data Grid）。它不仅提供了一系列的分布式的Java常用对象，还提供了许多分布式服务。其中包括(`BitSet`, `Set`, `Multimap`, `SortedSet`, `Map`, `List`, `Queue`, `BlockingQueue`, `Deque`, `BlockingDeque`, `Semaphore`, `Lock`, `AtomicLong`, `CountDownLatch`, `Publish / Subscribe`, `Bloom filter`, `Remote service`, `Spring cache`, `Executor service`, `Live Object service`, `Scheduler service`) Redisson提供了使用Redis的最简单和最便捷的方法。Redisson的宗旨是促进使用者对Redis的关注分离（Separation of Concern），从而让使用者能够将精力更集中地放在处理业务逻辑上。

简介：基于Redis的驻内存网络数据库，In-Memory Data Grid

https://github.com/redisson/redisson/wiki/目录

<img src="https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616152844107.png" alt="image-20200616152844107" style="zoom:50%;" />

强大1：提供的功能不仅仅包含了Redis所提供的，还提供了诸如延迟队列、分布式服务、多种分布式对象等！ 
强大2：很亲民（很多组件是基于JavaSE核心知识体系中的数据结构来提供服务的；面向Redis实现）

#### 基于ZooKeeper的分布式锁优化抢单逻辑

![image-20200616153816679](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616153816679.png)

### 其他优化点的介绍

要点1：雪花算法 ~不采用数据库主键自增的方式，减轻DB压力；避免同一时刻生成相同的订单号 
要点2：RabbitMQ业务模块解耦、异步通信 ~提高了接口的整体响应时间 

## 数据结构

`ModelMap`

```
ThreadLocalRandom
```

```
private static final SimpleDateFormat dateFormatOne=new SimpleDateFormat("yyyyMMddHHmmssSS");
```

```
StringBuffer 
```

```
BindingResult
```

```
cron 表达式
```

## 工具

postman

https://www.jianshu.com/p/77f4f9175028

Jmeter:

## 总结与建议

建议一：纸上得来终觉浅，绝知此事要躬行，一定要多敲，边敲边理解，边敲边思考 
建议二：整个课程完结之后自己要进行整体的回顾，包括整体的业务流程、出现的常见问题以及问题的解决方案！ 
建议三：对于“秒杀业务场景”中出现的问题的解决方案，还有很多，比如应用集群、应用服务器集群、中间件集群等等 
建议四：别想太多，有想法那就去实践（条件允许的前提下），只有实践才能出真知，只有实践才能学得更多、更有成长

## 页面效果

![image-20200616190647077](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616190647077.png)

![image-20200616190616745](https://gitee.com/XiaoShenKeHeBen/Static/raw/master/image-20200616190616745.png)