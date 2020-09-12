package com.github.xaidanwang.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;
//import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author wang yi fei
 * @date 2020/8/29 16:22
 */
public class ProtoBufffServer {
	public static void bind(int port) throws InterruptedException {
		// 配置服务端线程
		// EventLoopGroup 是一个 Executor 是管理线程的类，主用用来操作 线程类，启动线程类的工具
		// 里面包含了一个线程数组
		EventLoopGroup bossGroup = new NioEventLoopGroup(200);
		EventLoopGroup workGroup = new NioEventLoopGroup(400);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(workGroup,bossGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG,100)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							// 解码器
							pipeline.addLast(new ProtobufVarint32FrameDecoder());
							// ProtobufDecoder 不能同时解决两个结构体的编码解码
							pipeline.addLast(new ProtobufDecoder(AddressBookProtos.AddressBook.getDefaultInstance()));
//							pipeline.addLast(new ProtobufDecoder(SearchRequestProtos.SearchRequest.getDefaultInstance()));
							// 编码器
							pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
							pipeline.addLast(new ProtobufEncoder());
							pipeline.addLast(new ServerHandler(AddressBookProtos.AddressBook.class));
							pipeline.addLast(new ServerHandler2());
						}
					});
			// 绑定端口，同步等待成功
			ChannelFuture future = bootstrap.bind(port).sync();
//			ChannelPromise promise = future.channel().newPromise();
			// 等待服务端监听端口关闭
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		try {
			ProtoBufffServer.bind(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
