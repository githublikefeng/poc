package org.demoTcpServer.server;

import java.nio.charset.Charset;

import org.demoTcpServer.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
@Component
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	@Qualifier("tcpServerHandler")
	private ChannelHandler handler;
	@Autowired
	private Config config;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelHandler decoder = new StringDecoder(Charset.forName(config.getCharSet()));
		ChannelHandler encoder = new StringEncoder(Charset.forName(config.getCharSet()));
		
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("logging", new LoggingHandler(LogLevel.TRACE));
		pipeline.addLast(new LengthFieldBasedFrameDecoder(1024 * 10, 0, 4, 0, 0));
		pipeline.addLast(new LengthFieldPrepender(4));
		// 字符串解码 和 编码
		pipeline.addLast("decoder", decoder);
		pipeline.addLast("encoder", encoder);
		
		// 自己的逻辑Handler
		pipeline.addLast("handler", handler);
	}
}