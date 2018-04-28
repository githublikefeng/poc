package com.example.webservice.config;

import java.util.Map;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn(Bus.DEFAULT_BUS_ID)
public class WebServiceContextConfig implements ApplicationContextAware{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebServiceContextConfig.class);

	/**
	 * webservice发布服务统一管理器
	 * 
	 * @return
	 */
//	@Bean(name = Bus.DEFAULT_BUS_ID)
//	public SpringBus springBus() {
//		return new SpringBus();
//	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, WebServicePublisher> publishers = applicationContext.getBeansOfType(WebServicePublisher.class);
		for (WebServicePublisher publisher : publishers.values()) {
			Endpoint.publish(publisher.getAddress(),publisher);
			logger.info("发布服务[{}],绑定服务对象为[{}]",publisher.getAddress(),publisher);
		}
	}
	
}
