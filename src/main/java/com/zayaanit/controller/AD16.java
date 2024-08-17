package com.zayaanit.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CacusPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.CASMSType;
import com.zayaanit.exceptions.ServiceException;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.SMSResponse;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.OpcrnheaderRepo;
import com.zayaanit.repository.OpdoheaderRepo;
import com.zayaanit.util.SMSUtil;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/AD16")
public class AD16 extends KitController {

	private String pageTitle = null;

	@Autowired private ArhedRepo arhedRepo;
	@Autowired private CacusRepo cacusRepo;
	@Autowired private SMSUtil smsUtil;
	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpcrnheaderRepo opcrnheaderRepo;

	@Override
	protected String screenCode() {
		return "AD16";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD16"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(Model model) throws UnauthorizedException {
		return "pages/AD16/AD16";
	}

	@PostMapping("/store/{screen}")
	public @ResponseBody Map<String, Object> store(Integer code, @PathVariable String screen) throws UnauthorizedException {

		if(screen.equalsIgnoreCase("mr")) {
			Optional<Arhed> arhedOp = arhedRepo.findByXtrnnumAndXscreenAndZidAndXstatusIsIn(code, "FA31", sessionManager.getBusinessId(), Arrays.asList("Confirmed"));
			if(!arhedOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Money Receipt not found");
				return responseHelper.getResponse();
			}

			Arhed arhed = arhedOp.get();

			Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), arhed.getXcus()));
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found");
				return responseHelper.getResponse();
			}

			try {
				SMSResponse response = smsUtil.sendSMS(CASMSType.MONEY_RECEIPT, cacusOp.get(), smsUtil.prepareMoneyReceiptSMSText(cacusOp.get(), arhed));
				if(!"200".equalsIgnoreCase(response.getStatusCode())) {
					responseHelper.setErrorStatusAndMessage(response.getMessage());
					return responseHelper.getResponse();
				}
			} catch (ServiceException e) {
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}

		} else if (screen.equalsIgnoreCase("ca")) {
			Optional<Arhed> arhedOp = arhedRepo.findByXtrnnumAndXscreenAndZidAndXstatusIsIn(code, "FA32", sessionManager.getBusinessId(), Arrays.asList("Confirmed"));
			if(!arhedOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer Adjustment not found");
				return responseHelper.getResponse();
			}

			Arhed arhed = arhedOp.get();

			Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), arhed.getXcus()));
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found");
				return responseHelper.getResponse();
			}

			try {
				SMSResponse response = smsUtil.sendSMS(CASMSType.CUSTOMER_ADJUSTMENT, cacusOp.get(), smsUtil.prepareCustomerAdjustmentSMSText(cacusOp.get(), arhed));
				if(!"200".equalsIgnoreCase(response.getStatusCode())) {
					responseHelper.setErrorStatusAndMessage(response.getMessage());
					return responseHelper.getResponse();
				}
			} catch (ServiceException e) {
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}

		} else if (screen.equalsIgnoreCase("si")) {
			Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findByXdornumAndXstatusimAndZid(code, "Confirmed", sessionManager.getBusinessId());
			if(!opdoheaderOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Sales invoice not found");
				return responseHelper.getResponse();
			}

			Opdoheader op = opdoheaderOp.get();

			Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), op.getXcus()));
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found");
				return responseHelper.getResponse();
			}

			try {
				SMSResponse response = smsUtil.sendSMS(CASMSType.SALES_INVOICE, cacusOp.get(), smsUtil.prepareSalesInvoiceSMSText(cacusOp.get(), op));
				if(!"200".equalsIgnoreCase(response.getStatusCode())) {
					responseHelper.setErrorStatusAndMessage(response.getMessage());
					return responseHelper.getResponse();
				}
			} catch (ServiceException e) {
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
		} else if (screen.equalsIgnoreCase("sr")) {
			Optional<Opcrnheader> opdoheaderOp = opcrnheaderRepo.findByXcrnnumAndXstatusimAndZid(code, "Confirmed", sessionManager.getBusinessId());

			if(!opdoheaderOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Sales invoice not found");
				return responseHelper.getResponse();
			}

			Opcrnheader op = opdoheaderOp.get();

			Optional<Cacus> cacusOp = cacusRepo.findById(new CacusPK(sessionManager.getBusinessId(), op.getXcus()));
			if(!cacusOp.isPresent()) {
				responseHelper.setErrorStatusAndMessage("Customer not found");
				return responseHelper.getResponse();
			}

			try {
				SMSResponse response = smsUtil.sendSMS(CASMSType.SALES_RETURN, cacusOp.get(), smsUtil.prepareSalesReturnSMSText(cacusOp.get(), op));
				if(!"200".equalsIgnoreCase(response.getStatusCode())) {
					responseHelper.setErrorStatusAndMessage(response.getMessage());
					return responseHelper.getResponse();
				}
			} catch (ServiceException e) {
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
		}

		responseHelper.setSuccessStatusAndMessage("SMS send successfully");
		return responseHelper.getResponse();
	}
}
