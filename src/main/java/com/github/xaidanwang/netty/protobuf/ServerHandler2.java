package com.github.xaidanwang.netty.protobuf;

import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wang yi fei
 * @date 2020/8/31 10:47
 */
public class ServerHandler2 extends SimpleChannelInboundHandler<SearchRequestProtos.SearchRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx,SearchRequestProtos.SearchRequest msg) throws Exception {
		String str = JsonFormat.printer().print(AddressBookProtos.AddressBook.getDefaultInstance());
		System.out.println(str);
		System.out.println("====================");
		System.out.println(msg);
		System.out.println("====================");
		ctx.writeAndFlush(msg);
	}

	public static void main(String[] args) {
		int[] SIZE_TABLE;
		List<Integer> sizeTable = new ArrayList<Integer>();
		for (int i = 16; i < 512; i += 16) {
			sizeTable.add(i);
		}

		for (int i = 512; i > 0; i <<= 1) {
			sizeTable.add(i);
		}

		SIZE_TABLE = new int[sizeTable.size()];
		for (int i = 0; i < SIZE_TABLE.length; i ++) {
			SIZE_TABLE[i] = sizeTable.get(i);
		}
		System.out.println(sizeTable);
		int size = getSizeTableIndex(SIZE_TABLE,32);
		System.out.println(size);
	}

	private static int getSizeTableIndex(int[] SIZE_TABLE,final int size) {
		for (int low = 0, high = SIZE_TABLE.length - 1;;) {
			if (high < low) {
				return low;
			}
			if (high == low) {
				return high;
			}

			int mid = low + high >>> 1;
			int a = SIZE_TABLE[mid];
			int b = SIZE_TABLE[mid + 1];
			if (size > b) {
				low = mid + 1;
			} else if (size < a) {
				high = mid - 1;
			} else if (size == a) {
				return mid;
			} else {
				return mid + 1;
			}
		}
	}
}
