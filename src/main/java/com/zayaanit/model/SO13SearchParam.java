package com.zayaanit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Oct 6, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
public class SO13SearchParam implements Serializable {

	private static final long serialVersionUID = -6505241000742451543L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xfdate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xtdate;
	private Integer xbuid;
	private Integer xwh;
	private Integer xcus;
	private String xstatusord;

	private String businessUnitName;
	private String warehouseName;
	private String customerName;

	public static SO13SearchParam getDefaultInstance() {
		SO13SearchParam param = new SO13SearchParam();
		param.setXfdate(new Date());
		param.setXtdate(new Date());
		return param;
	}
}
