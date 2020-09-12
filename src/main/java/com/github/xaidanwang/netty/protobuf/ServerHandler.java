package com.github.xaidanwang.netty.protobuf;

import com.google.protobuf.MessageLite;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wang yi fei
 * @date 2020/8/31 10:47
 */
public class ServerHandler extends SimpleChannelInboundHandler< AddressBookProtos.AddressBook> {

	public ServerHandler(Class<? extends AddressBookProtos.AddressBook> inboundMessageType) {
		super(inboundMessageType);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx,  AddressBookProtos.AddressBook msg) throws Exception {
		String str = JsonFormat.printer().print(AddressBookProtos.AddressBook.getDefaultInstance());
		System.out.println(str);
		System.out.println("====================");
		System.out.println(msg);
		System.out.println("====================");
		ctx.writeAndFlush(msg);
		ctx.pipeline().remove(ServerHandler2.class);
		ServerHandler2 serverHandler2 = new ServerHandler2();
		ctx.pipeline().addLast(serverHandler2);
		ctx.pipeline().addLast(serverHandler2);
	}
}
