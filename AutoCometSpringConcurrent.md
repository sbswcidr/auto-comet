# 提升并发性能 #

建议使用java.util.concurrent里的工具来处理并发问题。

特别是针对大量只读操作的优化，可以用[ReentrantReadWriteLock](http://download.oracle.com/javase/6/docs/api/java/util/concurrent/locks/ReentrantReadWriteLock.html)来对代码块加锁。

请参考源码：[ChatRoomSocketHandler](http://code.google.com/p/auto-comet/source/browse/trunk/demo/cometChat/src/org/auto/comet/example/chat/service/ChatRoomSocketHandler.java)