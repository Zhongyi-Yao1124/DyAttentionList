实现关注列表应用

功能特性
四个标签页：互关、关注、粉丝、朋友（目前仅关注页面有具体实现）
在关注页面可以查看已关注的用户列表
可以点击关注按钮进行关注或取消关注
点击更多按钮(...)可以查看用户详情，并设置特别关注和备注
使用SQLite数据库存储用户数据和备注信息

项目结构
主要代码文件如下：
MainActivity：主活动，包含ViewPager2和TabLayout，用于切换四个Fragment。
MyAdapter：ViewPager2的适配器，管理四个Fragment的实例化。
PageAttentionFragment：关注页面的Fragment，显示已关注的用户列表。
PageEachotherFragment、PageFansFragment、PageFriendsFragment：其他三个页面的Fragment（尚未实现具体功能）。
AttentionAdapter：RecyclerView的适配器，用于显示用户列表。
User：用户实体类。
Remark：备注实体类。
DAO：数据访问对象，用于处理数据库操作。
SQLiteHelper：SQLite数据库帮助类，负责数据库的创建和升级。

使用说明
首次启动应用时，会初始化数据库并添加三个示例用户（周杰伦、王心凌、张韶涵）。
在关注页面，可以看到已关注的用户列表。
点击用户项的“关注”按钮可以取消关注，再次点击可以重新关注。
点击用户项的“更多”按钮（...）可以打开用户详情底部弹窗。
在用户详情弹窗中，可以设置特别关注和添加备注，也可以取消关注该用户。
