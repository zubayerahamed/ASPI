package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.PoordheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "poordheader")
@IdClass(PoordheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Poordheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536071L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private Integer xpornum;

	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xtotamt", precision = 15, scale = 2)
	private BigDecimal xtotamt;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusord", length = 25)
	private String xstatusord;

	@Column(name = "xgrnnum")
	private Integer xgrnnum;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Column(name = "xapprovertime")
	private Date xapprovertime;

	@Transient
	private String businessUnitName;
	@Transient
	private String supplierName;
	@Transient
	private String warehouseName;
	@Transient
	private String employeeName;
	@Transient
	private String staffName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Poordheader getDefaultInstance() {
		Poordheader obj = new Poordheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		return obj;
	}
}
