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

import com.zayaanit.entity.pk.ImtorheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "imtorheader")
@IdClass(ImtorheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "IM_TransferToIM", procedureName = "IM_TransferToIM", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "tornum", type = Integer.class),
	}),
	@NamedStoredProcedureQuery(name = "IM_createTO_fromTO", procedureName = "IM_createTO_fromTO", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "tornum", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.OUT, name = "newtor", type = Integer.class),
	})
})
public class Imtorheader extends AbstractModel<String> {

	private static final long serialVersionUID = 2194994045553532493L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private Integer xtornum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xorgim")
	private Integer xorgim;

	@Column(name = "xfwh")
	private Integer xfwh;

	@Column(name = "xtwh")
	private Integer xtwh;

	@Column(name = "xref", length = 25)
	private String xref;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xsadd", length = 200)
	private String xsadd;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime", nullable = true)
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xapprovertime ", nullable = true)
	private Date xapprovertime;

	@Column(name = "xapprnote", length = 200)
	private String xapprnote;

	@Column(name = "xscreen")
	private String xscreen;

	@Column(name = "xorigin")
	private String xorigin;

	@Transient
	private String fromStore;

	@Transient
	private String toStore;

	@Transient
	private String employee;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imtorheader getIM11DefaultInstance() {
		Imtorheader obj = new Imtorheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXscreen("IM11");
		obj.setXorigin("IM11");
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXdate(new Date());
		return obj;
	}

	public static Imtorheader getIM16DefaultInstance() {
		Imtorheader obj = new Imtorheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXscreen("IM11");
		obj.setXorigin("IM16");
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXdate(new Date());
		return obj;
	}
}
