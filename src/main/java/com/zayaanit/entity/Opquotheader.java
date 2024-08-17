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

import com.zayaanit.entity.pk.OpquotheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@Entity
@Table(name = "opquotheader")
@IdClass(OpquotheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "AD_addDefaultTerms", procedureName = "AD_addDefaultTerms", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "docnum", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "termstype", type = String.class)
	}) 
})
public class Opquotheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4026927678660702625L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xquotnum")
	private Integer xquotnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xorgop")
	private Integer xorgop;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xorg", length = 100)
	private String xorg;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xwhname", length = 100)
	private String xwhname;

	@Column(name = "xref", length = 100)
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

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xstafffrom")
	private Integer xstafffrom;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xorigin", length = 10)
	private String xorigin;

	@Column(name = "xemailbase", length = 100)
	private String xemailbase;

	@Column(name = "xemailto", length = 100)
	private String xemailto;

	@Column(name = "xemailcc", length = 100)
	private String xemailcc;

	@Column(name = "xemailbcc", length = 100)
	private String xemailbcc;

	@Column(name = "xsubject", length = 100)
	private String xsubject;

	@Column(name = "xsalute", length = 10)
	private String xsalute;

	@Column(name = "xbody1", length = 250)
	private String xbody1;

	@Column(name = "xbody2", length = 250)
	private String xbody2;

	@Column(name = "xbody3", length = 250)
	private String xbody3;

	@Column(name = "xdoreqnum")
	private Integer xdoreqnum;

	@Transient
	private String fromStaffName;
	@Transient
	private String customer;
	@Transient
	private String staff;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opquotheader getSO10DefaultInstance() {
		Opquotheader obj = new Opquotheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscp1(BigDecimal.ZERO);
		obj.setXdiscp2(BigDecimal.ZERO);
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXscreen("SO10");
		obj.setXorigin("SO10");
		return obj;
	}
}
