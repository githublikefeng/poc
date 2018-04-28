package com.example.http;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.demoCommons.SpeedMetric;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Response.AsyncContentListener;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class HttpClientSender implements InitializingBean{
	@Autowired
	private Config config;
	private static Semaphore count = new Semaphore(10000);
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public void start() throws Exception {
		// Instantiate and configure the SslContextFactory  
		SslContextFactory sslContextFactory = new SslContextFactory();  
		   
		// Instantiate HttpClient with the SslContextFactory  
		HttpClient httpClient = new HttpClient(sslContextFactory);  
		   
		// Configure HttpClient, for example:  
		httpClient.setFollowRedirects(false);  
		httpClient.setMaxConnectionsPerDestination(100);
		httpClient.setMaxRequestsQueuedPerDestination(count.availablePermits());
		// Start HttpClient  
		httpClient.start(); 
		String content = FileUtils.readFileToString(new File(config.getRequestFilePath()), config.getCharSet());
		long t0 = System.currentTimeMillis();
		int i = 0;
		while(i++<config.getTotal()){
			count.acquire();
			doSend(httpClient,content);
		}
		long t1 = System.currentTimeMillis();
		log.info("submit total {}, times {}",i,t1-t0);
	}

	private void doSend(HttpClient httpClient,String content) throws IOException {
		
		httpClient.POST(config.getUrl()).timeout(5, TimeUnit.SECONDS)
		.content(new StringContentProvider(content , config.getCharSet()))
//		.content(new PathContentProvider(Paths.get("e:/request.txt")), "text/plain")
		.onResponseContentAsync(new AsyncContentListener() {
			
			@Override
			public void onContent(Response response, ByteBuffer content, Callback callback) {
				SpeedMetric.handleRequest("httpClient");
				if (log.isInfoEnabled()) {
					try {
						byte[] dst = new byte[content.remaining()];
						content.get(dst);
						log.info(new String(dst,config.getCharSet()));
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				callback.succeeded();
			}
		}).onRequestFailure(new Request.FailureListener() {
			
			@Override
			public void onFailure(Request request, Throwable failure) {
//				failure.printStackTrace();
				log.error("send http fail ",failure);
			}
		}).send((result) ->{
			count.release();
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.start();
	}

}
