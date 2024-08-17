package com.zayaanit.service;

import com.zayaanit.entity.Casms;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.SMSResponse;
import com.zayaanit.model.SMSRequest;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
public interface SMSService {

	public SMSResponse sendSMS(SMSRequest req, Casms casms) throws ServiceException;
	public String getValidMobileNumber(String mobile);
}
