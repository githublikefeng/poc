package com.example.quasar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpHelper {
	static CloseableHttpClient httpClient = HttpClients.createDefault();
	public static String post(String str) throws IOException {
		//创建一个post对象
		HttpPost post = new HttpPost("http://localhost:8080/call/test.txt/utf8");
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("account", "admin"));  
        formparams.add(new BasicNameValuePair("password", str));  
        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");  
        post.setEntity(reqEntity);
		//执行post请求
		CloseableHttpResponse response =httpClient.execute(post);
		String string = EntityUtils.toString(response.getEntity());
//		System.out.println(string);
		response.close();
//		httpClient.close();
		return string;
	}
}
