package com.github.xaidanwang.netty.nio;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wang yi fei
 * @date 2020/9/10 11:14
 */
public class MultiplexerTimeServer implements Runnable {
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private volatile  boolean stop = false;

	public MultiplexerTimeServer(int  port) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(8080),1024);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("Time Server is start in port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		while (!stop){
			try {
				selector.select(1000);
				Set<SelectionKey> selectionKeySet =  selector.selectedKeys();
				Iterator<SelectionKey> it = selectionKeySet.iterator();
				while (it.hasNext()){
					SelectionKey selectionKey = it.next();
					it.remove();
					try {
						handleInput(selectionKey);
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 如果关闭 多路复用器，注册在该多路复用器上的所有的Channel 都会关闭
		CloseUtil.close(selector);
	}

	public void stop(){
		this.stop = true;
	}

	private void handleInput(SelectionKey selectionKey) throws IOException {
		if (selectionKey.isValid()){
			// 处理连接请求
			if (selectionKey.isAcceptable()){
				ServerSocketChannel serSocket =  (ServerSocketChannel)selectionKey.channel();
				SocketChannel sc = serSocket.accept();
				sc.configureBlocking(false);
				sc.register(selector,SelectionKey.OP_READ);
			}

			// 读取数据
			if (selectionKey.isReadable()){
				SocketChannel sc = (SocketChannel)selectionKey.channel();
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				// 写数据
				try {
					if (sc.read(readBuffer) > 0 ){
						// 翻转开始 从readBuffer 中读数据
						readBuffer.flip();
						byte[] bytes = new byte[readBuffer.remaining()];
						readBuffer.get(bytes);
						String body =  new String(bytes,"UTF-8");
						System.out.println("接收信息: " + body);
						if (body.equalsIgnoreCase("查询时间")){
							String res = "当前时间: " + LocalDateTime.now().toString();
							doWrite(sc,res);
						}
					}
				} catch (IOException e) {
					CloseUtil.close(sc);
				}
			}
		}
	}

	private void doWrite(SocketChannel sc,String res) throws IOException {
		if (!StringUtil.isNullOrEmpty(res)){
			ByteBuffer writerBuffer = ByteBuffer.wrap(res.getBytes());
			sc.write(writerBuffer);
		}
	}
}
