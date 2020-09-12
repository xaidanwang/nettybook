package com.github.xaidanwang.netty.nio;

/**
 * @author wang yi fei
 * @date 2020/9/10 11:24
 */
public class CloseUtil {


	public static void close(AutoCloseable autoCloseable){
		if (autoCloseable != null){
			try {
				autoCloseable.close();
				autoCloseable = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
