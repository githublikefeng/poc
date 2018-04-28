
package com.example.webservice.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.example.webservice.doman.Response;

@WebService(targetNamespace = "http://www.gzcb.org/example")
public interface SoapApi {

	@WebMethod(operationName = "GetReportSn")
	@WebResult(name = "GetReportSnResponse")
	public Response getReportSn(@WebParam(name="custName") String custName,@WebParam(name="mobileNo")  String mobileNo,@WebParam(name="idNo") String idNo);

}
