package com.github.xaidanwang.netty.nio;

/**
 * @author wang yi fei
 * @date 2020/9/10 14:04
 */
public class TimeClient {

	public static void main(String[] args) {

		new Thread(new TimeClientHandler(null,8080),"TimeClient-001").start();
	}
}
