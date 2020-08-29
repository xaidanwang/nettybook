package com.github.xaidanwang.netty.protobuf;

import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wang yi fei
 * @date 2020/8/29 17:23
 */
public class ClientHandler extends SimpleChannelInboundHandler<AddressBookProtos> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AddressBookProtos msg) throws Exception {

		JsonFormat.printer().print(AddressBookProtos.AddressBook.getDefaultInstance());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		AddressBookProtos.Person.PhoneNumber phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("17612102153").setType(AddressBookProtos.Person.PhoneType.HOME).build();
		AddressBookProtos.Person person = AddressBookProtos.Person.newBuilder().setName("王亦非").setEmail("wangx982770631@163.com").setId(1).setPhones(1,phoneNumber).build();
		AddressBookProtos.AddressBook addressBook = AddressBookProtos.AddressBook.newBuilder().setPeople(1,person).build();
		ctx.writeAndFlush(addressBook);
	}
}
