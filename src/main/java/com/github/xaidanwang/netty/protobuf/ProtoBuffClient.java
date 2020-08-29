package com.github.xaidanwang.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

/**
 * @author wang yi fei
 * @date 2020/8/29 16:21
 */
public class ProtoBuffClient {


	public static void connect(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(bossGroup).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY,false)
					//设置连接超时时间 3s
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ProtobufVarint32FrameDecoder());
							pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
							pipeline.addLast(new ProtobufEncoder());
						}
					});
			ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1",port));
			// 添加启动连接时出现连接异常，重连机制
//			future.addListener(new ConnectionListener());
			future.sync();
			future.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		try {
			ProtoBuffClient.connect(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
