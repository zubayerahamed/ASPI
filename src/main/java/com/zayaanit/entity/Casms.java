package com.zayaanit.entity;

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
import javax.persistence.Transient;

import com.zayaanit.entity.pk.CasmsPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Nov 30, 2023
 */
@Data
@Entity
@Table(name = "casms")
@IdClass(CasmsPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "Fn_GetSMSNumbers", procedureName = "Fn_GetSMSNumbers", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "cus", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "SMSNumbers", type = String.class)
	})
})
public class Casms extends AbstractModel<String> {

	private static final long serialVersionUID = -6956296362534185665L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xmask", length = 11, nullable = false)
	private String xmask;

	@Column(name = "xapi", length = 500, nullable = false)
	private String xapi;

	@Column(name = "xuserid", length = 100, nullable = false)
	private String xuserid;

	@Column(name = "xpassword", length = 100, nullable = false)
	private String xpassword;

	@Column(name = "xbase", length = 25, nullable = false)
	private String xbase;

	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Casms getDefaultInstance() {
		Casms obj = new Casms();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}

	public static Casms getDefaultInstance2(String xtype) {
		Casms obj = new Casms();
		obj.setXtype(xtype);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
