package com.example.http;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Response.AsyncContentListener;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpClientTest {
	static Semaphore count = new Semaphore(10000);
	public static void main(String[] args) throws Exception {
		// Instantiate and configure the SslContextFactory  
		SslContextFactory sslContextFactory = new SslContextFactory();  
		   
		// Instantiate HttpClient with the SslContextFactory  
		HttpClient httpClient = new HttpClient(sslContextFactory);  
		   
		// Configure HttpClient, for example:  
		httpClient.setFollowRedirects(false);  
		httpClient.setMaxRequestsQueuedPerDestination(count.availablePermits());
		// Start HttpClient  
		httpClient.start(); 
		String content = "hello";
		content = FileUtils.readFileToString(new File("e:/request.txt"), "utf8");
		long t0 = System.currentTimeMillis();
		int i = 0;
		while(i++<10*10000){
			count.acquire();
			doSend(httpClient,content);
		}
		long t1 = System.currentTimeMillis();
		System.out.println("over times "+(t1-t0));
	}

	private static void doSend(HttpClient httpClient,String content) throws IOException {
		
		httpClient.POST("http://localhost:80/call/respServerD.txt/utf8").timeout(5, TimeUnit.SECONDS)
		.content(new StringContentProvider(content , "gbk"))
//		.content(new PathContentProvider(Paths.get("e:/request.txt")), "text/plain")
		.onResponseContentAsync(new AsyncContentListener() {
			
			@Override
			public void onContent(Response response, ByteBuffer content, Callback callback) {
				try {
					byte[] dst = new byte[content.remaining()];
					content.get(dst);
//					System.out.println(new String(dst,"utf8"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					callback.succeeded();
				}
			}
		}).onRequestFailure(new Request.FailureListener() {
			
			@Override
			public void onFailure(Request request, Throwable failure) {
				failure.printStackTrace();
			}
		}).send((result) ->{
			Response response = result.getResponse();
			count.release();
		});
	}

}
