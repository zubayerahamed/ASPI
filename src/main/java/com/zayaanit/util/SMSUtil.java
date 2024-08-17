package com.zayaanit.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Casms;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.pk.CasmsPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.model.SMSRequest;
import com.zayaanit.model.SMSResponse;
import com.zayaanit.repository.CasmsRepo;
import com.zayaanit.service.KitSessionManager;
import com.zayaanit.service.SMSService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 5, 2023
 */
@Slf4j
@Service
public class SMSUtil {

	@Autowired private CasmsRepo casmsRepo;
	@Autowired private KitSessionManager sessionManager;
	@Autowired private SMSService smsService;

	public SMSResponse sendSMS(CASMSType smsType, Cacus cacus, String message) throws ServiceException {
		Optional<Casms> casmsOp = casmsRepo.findById(new CasmsPK(sessionManager.getBusinessId(), smsType.getPrompt()));
		if(!casmsOp.isPresent()) throw new ServiceException("SMS Config not found");
		if(!Boolean.TRUE.equals(cacus.getXissms())) throw new ServiceException("SMS not active for this customer : " + cacus.getXcus() + " - " + cacus.getXorg());

		Set<String> numbers = new HashSet<>();
		if(StringUtils.isNotBlank(cacus.getXphone())) numbers.add(cacus.getXphone());
		if(StringUtils.isNotBlank(cacus.getXsmsnos())) {
			String[] numbes = cacus.getXsmsnos().split(",");
			if(numbes.length > 0) {
				for(String number : numbes) {
					String phone = smsService.getValidMobileNumber(number);
					if(StringUtils.isBlank(phone)) continue;
					numbers.add(phone);
				}
			}
		}

		String procNumbers = casmsRepo.Fn_GetSMSNumbers(sessionManager.getBusinessId(), cacus.getXcus());
		if(StringUtils.isNotBlank(procNumbers)) {
			String[] numbes = procNumbers.split(",");
			if(numbes.length > 0) {
				for(String number : numbes) {
					String phone = smsService.getValidMobileNumber(number);
					if(StringUtils.isBlank(phone)) continue;
					numbers.add(phone);
				}
			}
		}

		if(numbers.isEmpty()) throw new ServiceException("Mobile numbers empty to send SMS for this customer : " + cacus.getXcus() + " - " + cacus.getXorg());

		// SEND SMS
		SMSResponse smsResponse = new SMSResponse();
		for(String mobile : numbers) {
			SMSRequest req = new SMSRequest();
			req.setMsisdn(Arrays.asList(mobile));
			req.setMessage(message);
			try {
				smsResponse = smsService.sendSMS(req, casmsOp.get());
				log.info("=======> SMS Response : {}", smsResponse.toString());
			} catch (ServiceException e) {
				log.error("====> SMS ERROR : {} ", e.getMessage());
				throw  new ServiceException(e.getMessage());
			}
		}

		return smsResponse;
	}

	public String prepareInvoiceCorrectionSMSText(Cacus cacus, Opdoheader op) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		StringBuilder smsText = new StringBuilder();
		smsText.append("Dear ");
		smsText.append(getCustomerName(cacus));
		smsText.append("("+ cacus.getXcus() +"), ");
		smsText.append("Invoice Correction, #" + op.getXdornum() + ", ");
		smsText.append("Date: " + sdf.format(new Date()) + ". ");
		smsText.append("Thanks. #LIRA");

		return smsText.toString();
	}

	public String prepareSalesInvoiceSMSText(Cacus cacus, Opdoheader op) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		StringBuilder smsText = new StringBuilder();
		smsText.append("Dear ");
		smsText.append(getCustomerName(cacus));
		smsText.append("("+ cacus.getXcus() +"), ");
		smsText.append("Transaction: Sales Invoice (Dr.) #" + op.getXdornum() + ", ");
		smsText.append("Amount: " + op.getXtotamt() + ", ");
		smsText.append("Date: " + sdf.format(op.getXdate()) + ". ");
		smsText.append("Thanks. #LIRA");

		return smsText.toString();
	}

	public String prepareSalesReturnSMSText(Cacus cacus, Opcrnheader op) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		StringBuilder smsText = new StringBuilder();
		smsText.append("Dear ");
		smsText.append(getCustomerName(cacus));
		smsText.append("("+ cacus.getXcus() +"), ");
		smsText.append("Transaction: Sales Return (Cr.) #" + op.getXcrnnum() + ", ");
		smsText.append("Amount: " + op.getXtotamt() + ", ");
		smsText.append("Date: " + sdf.format(op.getXdate()) + ". ");
		smsText.append("Thanks. #LIRA");

		return smsText.toString();
	}

	public String prepareCustomerAdjustmentSMSText(Cacus cacus, Arhed op) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		StringBuilder smsText = new StringBuilder();
		smsText.append("Dear ");
		smsText.append(getCustomerName(cacus));
		smsText.append("("+ cacus.getXcus() +"), ");
		smsText.append("Transaction: Customer Adjustment ("+ (op.getXsign() == -1 ? "Cr." : "Dr.") +") #" + op.getXtrnnum() + ", ");
		smsText.append("Amount: " + op.getXprime() + ", ");
		smsText.append("Date: " + sdf.format(op.getXdate()) + ". ");
		smsText.append("Thanks. #LIRA");

		return smsText.toString();
	}

	public String prepareMoneyReceiptSMSText(Cacus cacus, Arhed op) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

		StringBuilder smsText = new StringBuilder();
		smsText.append("Dear ");
		smsText.append(getCustomerName(cacus));
		smsText.append("("+ cacus.getXcus() +"), ");
		smsText.append("Transaction: Money Receipt (Cr.) #" + op.getXtrnnum() + ", ");
		smsText.append("Amount: " + op.getXprime() + ", ");
		smsText.append("Date: " + sdf.format(op.getXdate()) + ". ");
		smsText.append("Thanks. #LIRA");

		return smsText.toString();
	}

	private String getCustomerName(Cacus cacus) {
		return StringUtils.isNotBlank(cacus.getXorg()) && cacus.getXorg().length() > 30 ? cacus.getXorg().substring(0, 30) + " " : cacus.getXorg() + " ";
	}
}
