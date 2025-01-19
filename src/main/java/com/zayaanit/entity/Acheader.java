package com.zayaanit.entity;

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

import com.zayaanit.entity.pk.AcheaderPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acheader")
@IdClass(AcheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "FA_VoucherPost", procedureName = "FA_VoucherPost", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "voucher", type = Integer.class) 
	}),
	@NamedStoredProcedureQuery(name = "FA_VoucherUnPost", procedureName = "FA_VoucherUnPost", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "voucher", type = Integer.class) 
	}) 
})
public class Acheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4696242623656256597L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xvtype", length = 25)
	private String xvtype;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xper")
	private Integer xper;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Transient
	private String businessUnitName;
	@Transient
	private String staffName;
	
	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acheader getDefaultInstance() {
		Acheader obj = new Acheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
