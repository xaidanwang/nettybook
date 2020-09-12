package com.github.xaidanwang.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author wang yi fei
 * @date 2020/8/31 14:58
 */
public class CusatomMessageToMessageDecoder extends MessageToMessageDecoder<AddressBookProtos.AddressBook> {


	protected void decode(ChannelHandlerContext ctx, AddressBookProtos.AddressBook msg, List<Object> out) throws Exception {

	}
}
