package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.enums.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xusers")
@IdClass(XusersPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xusers extends AbstractModel<String> {

	private static final long serialVersionUID = -2612771101344210891L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 20)
	private String zemail;

	@Column(name = "xpassword", length = 20)
	private String xpassword;

	@Column(name = "xpasswordold", length = 20)
	private String xpasswordold;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xprofile", length = 20)
	private String xprofile;

	@Column(name = "xarea")
	private Integer xarea;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;

	@Column(name = "zadmin", length = 1)
	private Boolean zadmin;

	@Column(name = "xcusview")
	private Integer xcusview;

	@Column(name = "xorgpo")
	private Integer xorgpo;

	@Column(name = "xorgop")
	private Integer xorgop;

	@Column(name = "xorgim")
	private Integer xorgim;

	@Column(name = "xswbusiness", length = 1)
	private Boolean xswbusiness = Boolean.FALSE;

	@Transient
	private String roles;

	@Transient
	private String purOrg;

	@Transient
	private String salesOrg;

	@Transient
	private String inventoryOrg;

	public String getRoles() {
		this.roles = "";
		if (Boolean.TRUE.equals(zadmin))
			roles += UserRole.ZADMIN.getCode() + ',';

		if (StringUtils.isBlank(roles))
			return roles;

		int lastComma = roles.lastIndexOf(',');
		String finalString = roles.substring(0, lastComma);
		roles = finalString;
		return roles;
	}

	@Transient
	private String employee;

	@Transient
	private String area;

	@Transient
	private String businessName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xusers getDefaultInstance() {
		Xusers obj = new Xusers();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.FALSE);
		obj.setZactive(Boolean.TRUE);
		obj.setXcusview(30);
		return obj;
	}
}
