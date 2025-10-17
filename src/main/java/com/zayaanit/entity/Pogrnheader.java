package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

import com.zayaanit.entity.pk.PogrnheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for GRN Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "pogrnheader")
@IdClass(PogrnheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "PO_ConfirmGRN", procedureName = "PO_ConfirmGRN", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "grnnum", type = Integer.class), 
	}),
})
public class Pogrnheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536075L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xgrnnum")
	private Integer xgrnnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xpornum")
	private Integer xpornum;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xtotamt", precision = 15, scale = 2)
	private BigDecimal xtotamt;

	@Column(name = "xinvnum", length = 50)
	private String xinvnum;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xsubmittime")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Column(name = "xtype", length = 25)
	private String xtype;

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

	public static Pogrnheader getDefaultInstance() {
		Pogrnheader obj = new Pogrnheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXstatusjv("Open");
		obj.setXtype("Direct Purchase");
		return obj;
	}

	public static Pogrnheader getDefaultInstance(List<Cabunit> cabunits) {
		Pogrnheader obj = getDefaultInstance();
		if(cabunits != null && cabunits.size() == 1) {
			obj.setXbuid(cabunits.get(0).getXbuid());
			obj.setBusinessUnitName(cabunits.get(0).getXname());
		}
		return obj;
	}
}
