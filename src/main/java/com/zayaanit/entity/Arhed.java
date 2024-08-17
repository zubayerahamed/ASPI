package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.ArhedPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "arhed")
@IdClass(ArhedPK.class)
@EqualsAndHashCode(callSuper = true)
public class Arhed extends AbstractModel<String> {

	private static final long serialVersionUID = 3988634345324693682L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtrnnum")
	private Integer xtrnnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xbank")
	private Integer xbank;

	@Column(name = "xprime")
	private BigDecimal xprime;

	@Column(name = "xbranch", length = 100)
	private String xbranch;

	@Column(name = "xtypeobj", length = 100)
	private String xtypeobj;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xdocnum")
	private Integer xdocnum;

	@Column(name = "xdoctype", length = 25)
	private String xdoctype;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xapprovertime", nullable = true)
	private Date xapprovertime;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime", nullable = true)
	private Date xsubmittime;

	@Column(name = "xsign")
	private Integer xsign;

	@Column(name = "xtypetrn")
	private String xtypetrn;

	@Column(name = "xreason", length = 100)
	private String xreason;

	@Column(name = "xorg", length = 100)
	private String xorg;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xorigin", length = 10)
	private String xorigin;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdateact")
	private Date xdateact;

	@Transient
	private String customer;

	@Transient
	private String bank;

	@Transient
	private String employee;

	@Transient
	private String staff;

	@Transient
	private String location;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Arhed getFA31DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXdoctype("Money Receipt");
		obj.setXsign(-1);
		obj.setXtypetrn("Sale");
		obj.setXscreen("FA31");
		obj.setXorigin("FA31");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		return obj;
	}

	public static Arhed getAD20Tab1DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXscreen("FA31");
		obj.setXorigin("FA31");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXdateact(new Date());
		return obj;
	}

	public static Arhed getAD20Tab2DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXscreen("FA32");
		obj.setXorigin("FA32");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXdateact(new Date());
		return obj;
	}

	public static Arhed getAD20Tab3DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXscreen("FA36");
		obj.setXorigin("FA36");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXdateact(new Date());
		return obj;
	}

	public static Arhed getAD20Tab4DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXscreen("FA37");
		obj.setXorigin("FA37");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXdateact(new Date());
		return obj;
	}

	public static Arhed getFA32DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXdoctype("Customer Adjustment");
		obj.setXtypetrn("Sale");
		obj.setXscreen("FA32");
		obj.setXorigin("FA32");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXtypeobj("Credit");
		return obj;
	}

	public static Arhed getFA33DefaultInstance() {
		Arhed obj = new Arhed();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXdoctype("Opening Balance");
		obj.setXtypetrn("Sale");
		obj.setXscreen("FA33");
		obj.setXorigin("FA33");
		obj.setXprime(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXtypeobj("Normal");
		return obj;
	}
}
