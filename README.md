# PermissionsTime
### 支持跨服的权限限时插件

#### 构建地址:[http://ci.frog.gg/jenkins/job/PermissionsTime/](http://ci.frog.gg/jenkins/job/PermissionsTime/)

#### 已知问题(作者提醒):

如果出现问题一般退出服务器重进就能解决。一次不行再来一次。

- 如果权限组有继承关系, 被继承的权限组会被大组覆盖, 大组到期被删除, 小组的权限就没有了, 只能重新登录游戏。
- 服务器关闭时, 插件被禁用的顺序不一, 会导致玩家有权限留存。 如果配置的权限包删掉了原来设置的权限, 会导致插件无法清理原有的权限及权限组。(你可以配置一个不使用的权限包, 权限包内含有你想清理的权限及权限组即可。)

#### 插件进度：

加粗项已完成

- **在配置文件中编辑权限包(可含有多个权限组和多个权限)**
- **时间可以累加**
- **支持重载**
- **支持UUID**
- **命令支持给玩家添加、设置、移除、查询自身权限包时间**
- **添加、设置、移除-命令执行失败记录**
- **支持不同世界权限**
- **调用vault API 设置玩家权限 -- 登录时(添加 移除)、游戏中(命令添加/移除 延迟移除)、退出时移除**
- mysql保存数据
- 支持跨服
- 手动删除过期的或无效数据
- 语言支持整理
- 支持分页?

#### 使用统计：
![image](http://i.mcstats.org/PermissionsTime/Global+Statistics.borderless.png)