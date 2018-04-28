package org.demoTcpServer.server;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.demoCommons.SpeedMetric;
import org.demoTcpServer.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
@Sharable
@Component("tcpServerHandler")
public class TcpServerHandler extends SimpleChannelInboundHandler<String>{
	private static Logger logger = LoggerFactory.getLogger(TcpServerHandler.class);
	private static String respContext = null;
	@Autowired
	private Config config;
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info("tcp server in >>> {}" , msg);
		SpeedMetric.handleRequest("tcpServer");
		if (logger.isDebugEnabled()) {
			String requestFieldConfig = config.getRequestFieldConfig();
			String[] arr = requestFieldConfig.split(",");
			StringBuilder b = new StringBuilder("\r\n");
			byte[] buf = msg.getBytes(config.getCharSet());
			int offset = 0;
			for (String len : arr) {
				int length = Integer.parseInt(len);
				b.append(len).append(">>>").append(new String(buf,offset,length,config.getCharSet())).append("\r\n");
				offset+=length;
			}
			logger.debug(b.toString());
		}
		if (respContext == null) {
			respContext = loadRespContext();
		}
		ChannelFuture future = ctx.channel().writeAndFlush(respContext);
		future.addListener(new GenericFutureListener<Future<? super Void>>() {

			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				SpeedMetric.handleRequest("TcpServerHandler");
			}
		});
		future.addListener(ChannelFutureListener.CLOSE);
	}

	private String loadRespContext() throws IOException {
		File file = new File(config.getResponseFilePath());
		logger.info("load resp file {}",file);
		String resp = FileUtils.readFileToString(file, config.getCharSet());
		logger.info("load resp >>> {}",resp);
		if (logger.isDebugEnabled()) {
			String responseFieldConfig = config.getResponseFieldConfig();
			String[] arr = responseFieldConfig.split(",");
			StringBuilder b = new StringBuilder("\r\n");
			byte[] buf = resp.getBytes(config.getCharSet());
			int offset = 0;
			for (String len : arr) {
				int length = Integer.parseInt(len);
				b.append(len).append(">>>").append(new String(buf,offset,length,config.getCharSet())).append("\r\n");
				offset+=length;
			}
			logger.debug(b.toString());
		}
		return resp;
	}

}
