package com.github.xaidanwang.netty.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wang yi fei
 * @date 2020/9/9 17:33
 */
public class TimeServerHandler implements Runnable {

	private Socket socket;

	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		BufferedReader in = null;
		PrintWriter put = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			put = new PrintWriter(this.socket.getOutputStream());
			String body = null;
			while (true){
				body = in.readLine();
				System.out.println(body);
				if (body == null){
					break;
				}
				System.out.println("接收数据信息："+ body);
				if (body.equalsIgnoreCase("查询时间")){
					String res = "当前时间: " + LocalDateTime.now().toString() + "\n";
					put.write(res);
					put.flush();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			close(in);
			close(put);
			close(socket);
		}
	}


	private void close(AutoCloseable closeable){
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
