package com.zayaanit.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Xlogsdt implements Serializable {

	private static final long serialVersionUID = -1624354019825186611L;


	private Integer zid;
	private String xsession;
	private String zemail;
	private Integer xstaff;
	private Date ztime;
	private Date xdate;
	private String xscreen;
	private String xfunc;
	private String xsource;
	private String xtable;
	private String xdata;
	private String xstatement;
	private String xresult;
}
