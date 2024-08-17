package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.converter.CKTimeConverter;
import com.zayaanit.entity.pk.OpshipsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.util.CKTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 14, 2024
 */
@Data
@Entity
@Table(name = "opships")
@IdClass(OpshipsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opships extends AbstractModel<String> {

	private static final long serialVersionUID = 5480045632255862275L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xshipment")
	private Integer xshipment;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xtypeship", length = 25)
	private String xtypeship;

	@Column(name = "xvhl")
	private Integer xvhl;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xstaffdrv")
	private Integer xstaffdrv;

	@Column(name = "xnamedrv", length = 100)
	private String xnamedrv;

	@Column(name = "xhelper", length = 100)
	private String xhelper;

	@Column(name = "xincharge")
	private Integer xincharge;

	@Column(name = "xlocstart", length = 100)
	private String xlocstart;

	@Column(name = "xlocend", length = 100)
	private String xlocend;

	@Column(name = "xarea")
	private Integer xarea;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatein")
	private Date xdatein;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdateout")
	private Date xdateout;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatestart")
	private Date xdatestart;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdateend")
	private Date xdateend;

	@Column(name = "xmlstart")
	private BigDecimal xmlstart;

	@Column(name = "xmlend")
	private BigDecimal xmlend;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xapprovertime")
	private Date xapprovertime;

	@Column(name = "xpath", length = 500)
	private String xpath;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xorigin", length = 10)
	private String xorigin;

	@Column(name = "xstaffdm")
	private Integer xstaffdm;

	@Column(name = "xtotpoint")
	private Integer xtotpoint;

	@Column(name = "xtotship")
	private BigDecimal xtotship;

	@Transient
	@Convert(converter = CKTimeConverter.class)
	private CKTime xdateintime;

	@Transient
	@Convert(converter = CKTimeConverter.class)
	private CKTime xdateouttime;

	@Transient
	@Convert(converter = CKTimeConverter.class)
	private CKTime xdatestarttime;

	@Transient
	@Convert(converter = CKTimeConverter.class)
	private CKTime xdateendtime;

	@Transient
	private String incharge;

	@Transient
	private String deliveryMan;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	@Transient
	private String store;

	@Transient
	private String licence;

	public static Opships getDefaultInstance() {
		Opships obj = new Opships();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		return obj;
	}
}
