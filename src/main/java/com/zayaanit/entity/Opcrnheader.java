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

import com.zayaanit.entity.pk.OpcrnheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "opcrnheader")
@IdClass(OpcrnheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "SO_transferReturnToIM", procedureName = "SO_transferReturnToIM", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "crnnum", type = Integer.class),
	}),
	@NamedStoredProcedureQuery(name = "SO_transferReturnToAR", procedureName = "SO_transferReturnToAR", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "crnnum", type = Integer.class),
	}),
	@NamedStoredProcedureQuery(name = "SO_createReturnfromInvoice", procedureName = "SO_createReturnfromInvoice", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "crnnum", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "dornum", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
	})
})
public class Opcrnheader extends AbstractModel<String> {

	private static final long serialVersionUID = 2740012658470656397L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private Integer xcrnnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xorgop")
	private Integer xorgop;

	@Column(name = "xitemtype", length = 25)
	private String xitemtype;

	@Column(name = "xdornum")
	private Integer xdornum;

	@Column(name = "xcus")
	private Integer xcus;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

	@Column(name = "xdiscp1")
	private BigDecimal xdiscp1;

	@Column(name = "xdiscp2")
	private BigDecimal xdiscp2;

	@Column(name = "xdiscp3")
	private BigDecimal xdiscp3;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xref", length = 25)
	private String xref;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusar", length = 25)
	private String xstatusar;

	@Column(name = "xsadd", length = 200)
	private String xsadd;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xscreen")
	private String xscreen;

	@Column(name = "xorigin")
	private String xorigin;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatear")
	private Date xdatear;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime", nullable = true)
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Transient
	private String store;
	@Transient
	private String customer;
	@Transient
	private String staff;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opcrnheader getSO19DefaultInstance() {
		Opcrnheader obj = new Opcrnheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusar("Open");
		obj.setXstatusim("Open");
		obj.setXdate(new Date());
		obj.setXscreen("SO19");
		obj.setXorigin("SO19");
		obj.setXtype("Direct Return");
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscp1(BigDecimal.ZERO);
		obj.setXdiscp2(BigDecimal.ZERO);
		obj.setXdiscp3(BigDecimal.ZERO);
		return obj;
	}

	public static Opcrnheader getSO20DefaultInstance() {
		Opcrnheader obj = new Opcrnheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdiscp3(BigDecimal.ZERO);
		obj.setXdate(new Date());
		obj.setXscreen("SO20");
		obj.setXorigin("SO20");
		obj.setXtype("Invoice Return");
		return obj;
	}

}
