package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.zayaanit.entity.pk.XcodesPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed 
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xcodes")
@IdClass(XcodesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xcodes extends AbstractModel<String> {

	private static final long serialVersionUID = 405351171350622895L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xtype", length = 50)
	private String xtype;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xcode", length = 50)
	private String xcode;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@NotNull
	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xcodes getDefaultInstance() {
		Xcodes obj = new Xcodes();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.TRUE);
		return obj;
	}
}
