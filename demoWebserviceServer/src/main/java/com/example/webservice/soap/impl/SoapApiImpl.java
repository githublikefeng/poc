package com.example.webservice.soap.impl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.demoCommons.SpeedMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.webservice.config.WebServicePublisher;
import com.example.webservice.doman.Response;
import com.example.webservice.soap.SoapApi;

@WebService(endpointInterface = "com.example.webservice.soap.SoapApi", serviceName = "demoWebservice", targetNamespace = "http://www.gzcb.org/example")
@BindingType(SOAPBinding.SOAP12HTTP_MTOM_BINDING)
@Service
public class SoapApiImpl implements SoapApi, WebServicePublisher {

	private static final Logger log = LoggerFactory.getLogger(SoapApiImpl.class);

	@Override
	public Response getReportSn(String custName, String mobileNo,String idNo) {
		log.info("webservice in >>> custName [{}] , mobileNo [{}] , idNo [{}]", custName, mobileNo,idNo);
		SpeedMetric.handleRequest("webserviceServer");
		Response resp = new Response();
		resp.setCustName(custName);
		resp.setMobileNo(mobileNo);
		resp.setIdNo(idNo);
		resp.setReportSn("GZYH20171023150544078094");
		return resp;
	}

	@Override
	public String getAddress() {
		return "/test";
	}

}
