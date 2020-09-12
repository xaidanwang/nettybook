package com.github.xaidanwang.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wang yi fei
 * @date 2020/8/29 17:23
 */
public class ClientHandler2 extends SimpleChannelInboundHandler<SearchRequestProtos.SearchRequest> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SearchRequestProtos.SearchRequest msg) throws Exception {
		System.out.println("======SearchRequest======");
		System.out.println(msg);
		System.out.println("======SearchRequest======");
	}


	public static void main(String[] args) {
		Student class1 = new Student("1");
		Student class2 = new Student("1");
		System.out.println("class1"+class1.getClass());
		System.out.println("class2"+class2.getClass());
		System.out.println(class1 == class2);
	}
}
