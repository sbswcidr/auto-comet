# AutoComet与Spring的整合 #

> 如果你想与Spring整合，需要包含auto-comet-spring.jar。在comet服务配置中需要指定一个Spring的objectFactory。这样就可以将comet的handler配置为Spring的bean的名字。

dispatcher.spring.comet.xml配置
```
<?xml version="1.0" encoding="UTF-8"?>
<auto-comet xmlns="http://www.auto.org/schema/comet"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 

xmlns:spring="http://www.auto.org/schema/comet/spring"
	xsi:schemaLocation="http://www.auto.org/schema/comet http://www.auto.org/schema/comet/auto-comet-

1.0.xsd
	http://www.auto.org/schema/comet/spring http://www.auto.org/schema/comet/auto-comet-spring-

1.0.xsd">

	<!--autoComet可以用指定objectFactory获取bean -->
	<!--指定为objectFactory为org.auto.comet.spring.ObjectFactory，则comet会从spring容器中获取bean -->
	<property name="objectFactory" value="org.auto.comet.spring.ObjectFactory" />

	<!--与spring整合后handler的配置为bean的名字 -->
	<comet request="/chatRoom.comet" handler="chatRoomSocketHandler" />

</auto-comet>
```