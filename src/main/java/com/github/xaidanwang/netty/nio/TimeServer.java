package com.github.xaidanwang.netty.nio;

import org.joda.time.DateTimeField;
import org.joda.time.Instant;

/**
 * @author wang yi fei
 * @date 2020/9/9 17:54
 */
public class TimeServer {

	public static void main(String[] args) {

		new Thread(new MultiplexerTimeServer(8080),"NIO-MultiplexerTimeServer-001").start();
	}
}
