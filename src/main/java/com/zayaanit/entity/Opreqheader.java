package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.OpreqheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "opreqheader")
@IdClass(OpreqheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "SO_createSOfromRequisition", procedureName = "SO_createSOfromRequisition", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "doreqnum", type = Integer.class) 
	}) 
})
public class Opreqheader extends AbstractModel<String> {

	private static final long serialVersionUID = -7326978324546727402L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoreqnum")
	private Integer xdoreqnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xorgop")
	private Integer xorgop;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xref", length = 25)
	private String xref;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

	@Column(name = "xdiscp1")
	private BigDecimal xdiscp1;

	@Column(name = "xdiscp2")
	private BigDecimal xdiscp2;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusreq", length = 25)
	private String xstatusreq;

	@Column(name = "xordernum")
	private Integer xordernum;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xapprovertime")
	private Date xapprovertime;

	@Column(name = "xreasontype", length = 50)
	private String xreasontype;

	@Column(name = "xreason", length = 200)
	private String xreason;

	@Column(name = "xsadd", length = 200)
	private String xsadd;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xexpdel")
	private Date xexpdel;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xscreen")
	private String xscreen;

	@Column(name = "xorigin")
	private String xorigin;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xitemtype", length = 25)
	private String xitemtype;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatear")
	private Date xdatear;

	@Transient
	private String customer;

	@Transient
	BigDecimal creditLimit;

	@Transient
	private String store;

	@Transient
	private String staff;

	@Transient
	private String staffAppr;

	@Transient
	private BigDecimal balance;

	@Transient
	private String location;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opreqheader getSO12DefaultInstance() {
		Opreqheader obj = new Opreqheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscp1(BigDecimal.ZERO);
		obj.setXdiscp2(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusreq("Open");
		obj.setXscreen("SO12");
		obj.setXorigin("SO12");
		obj.setXdate(new Date());
		return obj;
	}

	public static Opreqheader getSO11DefaultInstance() {
		Opreqheader obj = new Opreqheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscp1(BigDecimal.ZERO);
		obj.setXdiscp2(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusreq("Open");
		obj.setXscreen("SO12");
		obj.setXorigin("SO12");
		obj.setXdate(new Date());
		return obj;
	}
}
