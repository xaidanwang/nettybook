package com.github.xaidanwang.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wang yi fei
 * @date 2020/9/10 12:33
 */
public class TimeClientHandler implements Runnable{
	private String host;
	private int  port;
	private Selector selector;
	private SocketChannel sc;
	private volatile boolean stop = false;

	public TimeClientHandler(String host,int port) {
		this.host = host == null ? "127.0.0.1":host;
		this.port = port;
		try {
			this.selector = Selector.open();
			this.sc = SocketChannel.open();
			sc.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		try {
			doConnect();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		while (!stop){
			try {
				selector.select(1000);
				Set<SelectionKey> selectionKeySet = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeySet.iterator();
				while (iterator.hasNext()){
					SelectionKey selectionKey = iterator.next();
					iterator.remove();
					handleInput(selectionKey);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CloseUtil.close(selector);
	}

	private void doConnect() throws IOException {
		boolean connect = this.sc.connect(new InetSocketAddress(host,port));
		if (connect){
			this.sc.register(selector, SelectionKey.OP_READ);
		}else {
			this.sc.register(selector,SelectionKey.OP_CONNECT);
		}
	}

	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()){
			SocketChannel sc = (SocketChannel)key.channel();
			if (key.isConnectable()){
				if (sc.finishConnect()){
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				}else {
					System.exit(1);
				}
			}
			if (key.isReadable()){
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int size = sc.read(readBuffer);
				if (size > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body =  new String(bytes,"UTF-8");
					System.out.println("接收换回信息: " + body);
					this.stop = true;
				}else if (size < 0){
					// 对端链路关闭
					key.cancel();
					CloseUtil.close(sc);
				}
			}
		}
	}

	private void doWrite(SocketChannel sc) throws IOException {
		ByteBuffer writeBuffer = ByteBuffer.wrap("查询时间".getBytes());
//		writeBuffer.flip();
		sc.write(writeBuffer);
		if (!writeBuffer.hasRemaining()){
			System.out.println("发送2条命令到Server端");
		}
	}
}
