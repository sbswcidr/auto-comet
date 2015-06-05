**简介**

> comet是一种web服务器主动与浏览器通信的技术。可以用于web聊天,邮件提醒等场景。

> auto-comet是基于javaEE servlet3.0的comet框架。auto-comet亦在帮助你简单、快速的构建高效、安全的comet服务。

> 基于异步servlet的auto-comet具有占用服务器资源少且跨平台的优点。

> auto-comet需要运行在实现了servlet3.0规范的容器中，比如tomcat7.0及以上版本。

> 目前版本为1.0bate，欢迎你的加入！

**[WIKI](MainPage.md)**

**[快速开始](UserManual.md)**

**[推送协议](AutoCometPushProtocol.md)**

**[演示工程](CometDemo.md)**  在线示例：http://auto-comet.zzlkk.com/pages/chatRoom.html

**[提交bug](http://code.google.com/p/auto-comet/issues/list)**


**源码**

> 源码SVN下载地址： https://auto-comet.googlecode.com/svn/trunk/

> 源码编码：utf-8

> 编译版本：jdk1.6

> 源码一共有三个用Maven管理的工程：

> source目录下:
    * auto-comet [java工程]，是auto-comet的服务器端支持。
    * auto-comet-spring [java工程]，是auto-comet的spring整合支持。

> demo目录下：
    * cometChat [web工程]，是auto-comet的演示工程，包含客户端的js支持。springMVC+Spring+Hibernate。

**用户**
  * [szee.cc](http://szee.cc/)

**相关参考**

  * 关键字：comet，异步servlet，长轮询，服务器推技术，Cometd，bayeux协议，jetty continuations

  * 文章参考：
    1. Servlet 3.0 实战：异步 Servlet 与 Comet 风格应用程序:http://www.ibm.com/developerworks/cn/java/j-lo-comet/
    1. 使用 Java 实现 Comet 风格的 Web 应用:http://www.ibm.com/developerworks/cn/web/wa-cometjava/
    1. Cometd & Jetty Continuations:http://geeklu.com/2010/07/cometd-jetty-continuations/

**联系我们**
  * QQ群  : 179199183
  * Mail  : huhang1986@gmail.com

