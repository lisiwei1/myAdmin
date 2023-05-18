此项目是一个比较简单的前后端管理系统，技术栈是spring boot + vue2 。

此项目亮点有：

1、前端支持按钮级别的权限控制

2、利用AOP 处理日志，生成到每一个web请求相关的所有数据的日志，同时也支持记录定时任务的日志，方便接入ELK。日志格式如下

```json
{
  "classMethod": "api",
  "hostName": "LAPTOP-572VT37S",
  "consumeTime": 57,
  "hostIp": "192.168.0.118",
  "responseTime": "2023-05-06 13:39:35.840",
  "requestParams": "{\"code\":\"22\"}",
  "serverName": "admin-server-01",
  "requestIp": "127.0.0.1",
  "className": "com.logdemo.test.web.TestController",
  "httpMethod": "GET",
  "url": "http://127.0.0.1:20230/test/api",
  "sqls": [],
  "requestTime": "2023-05-06 13:39:35.783",
  "responseParams": "调用成功！",
  "traceToken": "e9ff6423-d14c-470e-be32-3f953f89b2f3",
  "currentOrder": 1,
  "desc": "测试API接口"
}
```

