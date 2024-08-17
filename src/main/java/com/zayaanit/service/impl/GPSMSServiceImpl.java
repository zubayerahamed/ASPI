package com.zayaanit.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zayaanit.entity.Casms;
import com.zayaanit.enums.GPMessageType;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.GPSMSReqBody;
import com.zayaanit.model.SMSResponse;
import com.zayaanit.model.SMSRequest;
import com.zayaanit.service.SMSService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
@Slf4j
@Service("GP_SMSService")
public class GPSMSServiceImpl implements SMSService {

	private static final String BASE_URL = "https://gpcmp.grameenphone.com/ecmapigw/webresources/ecmapigw.v2";

	@Override
	public SMSResponse sendSMS(SMSRequest req, Casms casms) throws ServiceException {
		if(req == null) throw new ServiceException("SMS requet body is empty");
		if(StringUtils.isBlank(req.getMessage())) throw new ServiceException("Message body is empty");
		if(req.getMsisdn() == null || req.getMsisdn().isEmpty()) throw new ServiceException("Contacts number not set to send SMS");

		GPSMSReqBody gpReq = new GPSMSReqBody();
		gpReq.setUsername(casms.getXuserid());
		gpReq.setPassword(casms.getXpassword());
		gpReq.setApicode("1");
		gpReq.setCountrycode("880");
		gpReq.setCli(casms.getXmask());
		gpReq.setMsisdn(req.getMsisdn());
		gpReq.setMessagetype(GPMessageType.ENGLISH.getValue());
		gpReq.setMessage(req.getMessage());
		gpReq.setMessageid("0");

		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = mapper.writeValueAsString(gpReq);
		} catch (JsonProcessingException e) {
			throw new ServiceException(e.getMessage());
		}
		if(jsonString == null) throw new ServiceException("Can't found any json object to send SMS throw GP");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(jsonString, headers);
		String responseBody = restTemplate.postForObject(StringUtils.isBlank(casms.getXapi()) ? casms.getXapi() : BASE_URL, entity, String.class);

		SMSResponse response = new SMSResponse();
		try {
			response = mapper.readValue(responseBody, SMSResponse.class);
		} catch (JsonProcessingException e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
		}

		return response;
	}

	@Override
	public String getValidMobileNumber(String mobile) {
		mobile = mobile.trim();
		if(mobile.startsWith("+")) mobile = mobile.replace("+", "");
		if(mobile.startsWith("88")) mobile = mobile.substring(2);
		if(mobile.length() == 10) mobile = "0".concat(mobile);
		if(mobile.length() != 11) return null;
		return mobile;
	}
}
