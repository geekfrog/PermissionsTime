# PermissionsTime
### 支持跨服的权限限时插件

#### 构建地址:[http://ci.frog.gg/jenkins/job/PermissionsTime/](http://ci.frog.gg/jenkins/job/PermissionsTime/)
#### 前置插件SQLibrary下载地址:[https://dev.bukkit.org/projects/sqlibrary](https://dev.bukkit.org/projects/sqlibrary/)
#### [插件使用说明](https://github.com/geekfrog/PermissionsTime/wiki/PermissionsTime-%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)

#### 已知问题(作者提醒):

- 如果出现问题一般退出服务器重进就能解决。一次不行再来一次。
- 插件重载不会重置在线玩家的权限, 如用必要请重启服务器。
- 玩家每次登陆会重置权限包涉及的权限。请不要单独给予玩家涉及的权限。
- 服务器关闭时, 插件被禁用的顺序不一, 会导致玩家有权限留存。 如果配置的权限包删掉原来设置的权限(或修改世界), 会导致插件无法清理原有的权限及权限组。(你可以配置一个不使用的权限包, 权限包内含有你想清理的权限及权限组即可。)

#### 插件进度：

加粗项已完成

- **在配置文件中编辑权限包(可含有多个权限组和多个权限)**
- **时间可以累加**
- **支持重载**
- **支持UUID**
- **命令支持给玩家添加、设置、移除、查询自身权限包时间**
- **添加、设置、移除-命令执行失败记录**
- **支持多世界权限(需要权限插件支持)**
- **调用vault API 设置玩家权限 -- 登录时(添加 移除)、游戏中(命令添加/移除 延迟移除)、退出时移除**
- **mysql保存数据**
- **支持跨服**
- **gui显示自己的权限包**
- **语言支持整理**
- 手动删除过期的或无效数据
- 取消前置插件


- 支持分页?
- 数据迁移?

#### 使用统计：
![image](http://i.mcstats.org/PermissionsTime/Global+Statistics.borderless.png)
