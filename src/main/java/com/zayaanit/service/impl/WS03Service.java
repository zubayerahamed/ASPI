package com.zayaanit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.model.WS03Dto;
import com.zayaanit.repository.OpdoheaderRepo;

/**
 * @author Zubayer Ahaned
 * @since Jul 21, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class WS03Service extends AbstractGenericService {

	@Autowired private OpdoheaderRepo opdoheaderRepo;

	public List<WS03Dto> itemSalesStatement(Integer xbuid, Integer xitem, Integer last, String type, String xfdate, String xtdate) {

		if("DAYS".equalsIgnoreCase(type)) {
			if(last > 100) last = 100;
			List<WS03Dto> result = opdoheaderRepo.getItemSalesStatementForDays(sessionManager.getBusinessId(), xbuid, xitem, last)
					.stream()
					.map(row -> new WS03Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
					.collect(Collectors.toList());
			return result;
		} 

		List<WS03Dto> result = opdoheaderRepo.getItemSalesStatementForDateBetween(sessionManager.getBusinessId(), xbuid, xitem, xfdate, xtdate)
				.stream()
				.map(row -> new WS03Dto(((Date) row[0]).toString(), (BigDecimal) row[1]))
				.collect(Collectors.toList());
		return result;
	}
}
