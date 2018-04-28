package org.demoTcpServer.server;

import org.demoTcpServer.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Service
public class TcpServer implements InitializingBean {
	private Logger log = LoggerFactory.getLogger(TcpServer.class);
	/**
	 * 服务端监听的端口地址
	 */
	private static final int portNumber = 7879;
	@Autowired
	private Config config;

	@Autowired
	private TcpServerInitializer tcpServerInitializer;
	public static void main(String[] args) throws InterruptedException {
		new TcpServer().start(portNumber);
	}

	public void start(int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(tcpServerInitializer);

			// 服务器绑定端口监听
			ChannelFuture f = b.bind(port).sync();
			log.info("server start on {}",port);
			// 监听服务器关闭监听
			f.channel().closeFuture().sync();

			// 可以简写为
			/* b.bind(portNumber).sync().channel().closeFuture().sync(); */
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start(config.getPort());
	}
}
