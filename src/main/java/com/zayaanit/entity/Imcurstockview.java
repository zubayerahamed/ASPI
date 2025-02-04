package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@Entity
@Table(name = "imcurstockview")
@EqualsAndHashCode(callSuper = true)
public class Imcurstockview extends AbstractModel<String> {

	private static final long serialVersionUID = 1685761788841395819L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xdesc")
	private String xdesc;

	@Column(name = "xgitem")
	private String xgitem;

	@Column(name = "xunit")
	private String xunit;

	@Column(name = "xcatitem")
	private String xcatitem;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xval")
	private BigDecimal xval;
}
