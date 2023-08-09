## EXP Introduction

extension plugin 扩展点插件系统

场景:
1. B 端大客户对业务进行定制, 需要对主代码扩展. 
   - 传统做法是 git 拉取分支.
   - 现在基于扩展点的方式进行定制, 可热插拔

## Feature

1. 支持 spring 热插拔
2. 基于 classloader 类隔离
3. 支持多租户场景下的多实现
