package com.github.xaidanwang.netty.protobuf;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author wang yi fei
 * @date 2020/8/29 17:23
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<AddressBookProtos.AddressBook> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, AddressBookProtos.AddressBook msg) throws Exception {
		System.out.println("====AddressBook=======");
		System.out.println(msg);
		System.out.println("====AddressBook=======");
/*		SearchRequestProtos.SearchRequest searchRequest = SearchRequestProtos.SearchRequest.newBuilder().setPageNumber(1).setQuery("12312312").setResultPerPage(10).build();
		System.out.println(searchRequest);
		ctx.writeAndFlush(searchRequest);*/
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		AddressBookProtos.Person john =
				AddressBookProtos.Person.newBuilder()
						.setId(1234)
						.setName("John Doe")
						.setEmail("jdoe@example.com")
						.addPhones(
								AddressBookProtos.Person.PhoneNumber.newBuilder()
										.setNumber("555-4321")
										.setType(AddressBookProtos.Person.PhoneType.HOME))
						.build();
		AddressBookProtos.Person john2 =
				AddressBookProtos.Person.newBuilder()
						.setId(1234)
						.setName("Aidan Wang")
						.setEmail("wang@example.com")
						.addPhones(
								AddressBookProtos.Person.PhoneNumber.newBuilder()
										.setNumber("1555678966")
										.setType(AddressBookProtos.Person.PhoneType.MOBILE))
						.build();
//		Any any = Any.getDefaultInstance();
//		AddressBookProtos a;
//		if (any.is(AddressBookProtos.Person .class)) {
//			john = any.unpack(AddressBookProtos.Person.class);
//		}
		AddressBookProtos.AddressBook addressBook = AddressBookProtos.AddressBook.newBuilder().addPeople(john).addPeople(john2).build();
		System.out.println(addressBook);
		ChannelFuture channelFuture = ctx.writeAndFlush(addressBook);
		if (channelFuture.isSuccess()){
			System.out.println("发送成功!");
		}
		long startTime = System.currentTimeMillis();
/*
		SearchRequestProtos.SearchRequest searchRequest = SearchRequestProtos.SearchRequest.newBuilder().setPageNumber(1).setQuery("12312312").setResultPerPage(10).build();
		System.out.println(searchRequest);
		ctx.writeAndFlush(searchRequest);*/
	}
}
