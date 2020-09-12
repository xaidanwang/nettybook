package com.github.xaidanwang.netty.bio;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.*;

/**
 * @author wang yi fei
 * @date 2020/9/9 17:29
 */
public class TimServer {

	private static ThreadFactory name = new ThreadFactoryBuilder().setNameFormat("测试1").build();
	private static ExecutorService executorService = new ThreadPoolExecutor(0,10,30L, TimeUnit.HOURS,new LinkedBlockingQueue(500),name,new ThreadPoolExecutor.DiscardPolicy());
	public static void main(String[] args) {
		int  port = 8080;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress("127.0.0.1",port));
			while (true){
				Socket socket = serverSocket.accept();
				executorService.execute(new TimeServerHandler(socket));
//				new Thread(new TimeServerHandler(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (serverSocket != null){
				System.out.println("关闭 serverSocket");
				try {
					serverSocket.close();
					serverSocket = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
