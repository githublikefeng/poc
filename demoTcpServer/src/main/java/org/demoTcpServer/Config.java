package org.demoTcpServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config {
	@Value("${server.port:7879}")
	private int port;

	@Value("${response.file.charSet:gbk}")
	private String charSet = "gbk";
	
	@Value("${response.file}")
	private String responseFilePath;
	
	@Value("${request.field.length.arr}")
	private String requestFieldConfig;
	
	@Value("${response.field.length.arr}")
	private String responseFieldConfig;
	
	public int getPort() {
		return port;
	}

	public String getResponseFilePath() {
		return responseFilePath;
	}
	
	public String getCharSet() {
		return charSet;
	}

	public String getRequestFieldConfig() {
		return requestFieldConfig;
	}

	public String getResponseFieldConfig() {
		return responseFieldConfig;
	}
	
}
