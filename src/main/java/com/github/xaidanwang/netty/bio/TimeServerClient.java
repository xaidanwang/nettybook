package com.github.xaidanwang.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author wang yi fei
 * @date 2020/9/9 17:42
 */
public class TimeServerClient {


	public static void main(String[] args) {
		BufferedReader in = null;
		PrintWriter put = null;
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("127.0.0.1",8080));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			put = new PrintWriter(socket.getOutputStream());
			put.write("查询时间\n");
			put.flush();
			String body = in.readLine();
			System.out.println(body);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			close(in);
			close(put);
			close(socket);
		}
	}

	private static void close(AutoCloseable closeable){
		if (closeable != null){
			try {
				closeable.close();
				closeable = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
