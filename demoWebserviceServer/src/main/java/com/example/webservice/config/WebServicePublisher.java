package com.example.webservice.config;

public interface WebServicePublisher{
	/**
	 * 服务路由路径，从应用名后开始算，比如：/testAddress
	 * <p>该服务发布出来的服务路径将是：http://ip:port/applicationName/testAddress</p>
	 * @return
	 */
	String getAddress();
}
