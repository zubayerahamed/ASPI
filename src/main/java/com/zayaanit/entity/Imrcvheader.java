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

import com.zayaanit.entity.pk.ImrcvheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "imrcvheader")
@IdClass(ImrcvheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "IM_transferReceiveToIM", procedureName = "IM_transferReceiveToIM", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "rcvnum", type = Integer.class),
	})
})
public class Imrcvheader  extends AbstractModel<String> {

	private static final long serialVersionUID = -3822016963563597271L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrcvnum")
	private Integer xrcvnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xorgim")
	private Integer xorgim;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xrcvtype", length = 25)
	private String xrcvtype;

	@Column(name = "xtotamt")
	private BigDecimal xtotamt;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xref", length = 25)
	private String xref;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xscreen")
	private String xscreen;

	@Column(name = "xorigin")
	private String xorigin;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "xsubmittime", nullable = true)
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Transient
	private String store;

	@Transient
	private String employee;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imrcvheader getIM12DefaultInstance() {
		Imrcvheader obj = new Imrcvheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXscreen("IM12");
		obj.setXorigin("IM12");
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXdate(new Date());
		return obj;
	}
}
