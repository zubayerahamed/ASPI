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

import com.zayaanit.entity.pk.PoordheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@Entity
@Table(name = "poordheader")
@IdClass(PoordheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "PO_CreateGRNfromOrder", procedureName = "PO_CreateGRNfromOrder", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "pornum", type = Integer.class), 
	}),
})
public class Poordheader extends AbstractModel<String> {

	private static final long serialVersionUID = -4564620786450303044L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private Integer xpornum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
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

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffappr")
	private Integer xstaffappr;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
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
	private String submitStaffName;
	@Transient
	private String apprStaffName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Poordheader getDefaultInstance() {
		Poordheader obj = new Poordheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXtotamt(BigDecimal.ZERO);
		return obj;
	}
}
