## EXP Introduction

extension plugin 扩展点插件系统

名词定义:
1. 主应用
2. 插件
3. 扩展点
4. 热插拔

场景:
1. B 端大客户对业务进行定制, 需要对主代码扩展. 
   - 传统做法是 git 拉取分支.
   - 现在基于扩展点的方式进行定制, 可热插拔
2. 多个程序可分可和部署, 支持将多个 springboot 应用合并部署, 或拆开部署.
3. 支持扩展点类似 swagger 文档 doc, 用于类插件系统管理平台进行展示.

## Feature

1. 支持 spring 热插拔
2. 基于 classloader 类隔离
3. 支持多租户场景下的多实现
4. 支持 springboot2.x/1.x 依赖


## USE

主程序依赖
```xml
<dependency>
   <groupId>cn.think.in.java</groupId>
   <artifactId>open-exp-adapter-springboot2</artifactId>
</dependency>
```

插件依赖
```xml
<dependency>
   <groupId>cn.think.in.java</groupId>
   <artifactId>open-exp-plugin-depend</artifactId>
</dependency>
```


## API 使用
```java
ExpAppContext expAppContext = ExpAppContextSpiFactory.getFirst();

public String run() {
     Optional<UserService> first = expAppContext.get(UserService.class).stream().findFirst();
     if (first.isPresent()) {
         first.get().createUserExt();
     } else {
         return "not found";
     }
     return "success";
 }

 public String install(String path) throws Throwable {
     Plugin load = expAppContext.load(new File(path));
     return load.getPluginId();
 }

 public String unInstall(String id) throws Exception {
     expAppContext.unload(id);
     return "ok";
 }
```
