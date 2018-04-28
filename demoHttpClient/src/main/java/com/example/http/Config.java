package com.example.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config {
	@Value("${request.total}")
	private int total;
	@Value("${request.url}")
	private String url;
	@Value("${request.file.charSet}")
	private String charSet;
	@Value("${request.file}")
	private String requestFilePath;
	public int getTotal() {
		return total;
	}
	public String getUrl() {
		return url;
	}
	public String getCharSet() {
		return charSet;
	}
	public String getRequestFilePath() {
		return requestFilePath;
	}
	
}
