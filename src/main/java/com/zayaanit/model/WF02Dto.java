package com.zayaanit.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Jul 22, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Builder
public class WF02Dto {

	private Date xdate;
	private BigDecimal amount;
	
	public WF02Dto(Date xdate, BigDecimal amount) {
		this.xdate = xdate;
		this.amount = amount;
	}
}
