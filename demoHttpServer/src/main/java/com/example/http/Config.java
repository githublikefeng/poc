package com.example.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config{
	@Value("${response.file.charSet:utf8}")
	private String charSet;
	@Value("${response.file.dir}")
	private String responseFileDir;
	@Value("${response.millitime.delay:0}")
	private long delay;
	
	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getResponseFileDir() {
		return responseFileDir;
	}

	public long getDelay() {
		return delay;
	}
}
