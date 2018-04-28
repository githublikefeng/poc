package com.example.http;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.demoCommons.SpeedMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HttpController {
	private Logger log = LoggerFactory.getLogger(HttpController.class);
	@Autowired
	private Config config;
	private Map<String, String> respMap = new ConcurrentHashMap<>();
	
	@RequestMapping("/call/{fileName}/{charSet}")
	@ResponseBody
	public String server(@PathVariable("fileName") String fileName,@PathVariable("charSet") String charSet,@RequestBody String body){
		log.info("server in >>> {}",body);
		SpeedMetric.handleRequest("httpServer");
		if (config.getDelay()>0) {
			try {
				TimeUnit.MILLISECONDS.sleep(config.getDelay());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return getResponse(fileName,charSet);
	}
	
	@RequestMapping("/call/{fileName}/{charSet}/reflush")
	@ResponseBody
	public String clear(@PathVariable("fileName") String fileName,@PathVariable("charSet") String charSet){
		log.info("delete resp by {}",fileName);
		respMap.remove(fileName);
		return getResponse(fileName,charSet);
	}
	
	private String getResponse(String fileName, String charSet) {
		String resp = respMap.get(fileName);
		if (resp == null) {
			File file = new File(config.getResponseFileDir(), fileName);
			try {
				log.info("load resp from {}",file);
				resp = FileUtils.readFileToString(file, charSet);
				respMap.put(fileName, resp);
			} catch (IOException e) {
				log.error("read file error {}", file,e);
			}
		}
		return resp;
	}
	
}
