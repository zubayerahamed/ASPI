package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.CaitemorgPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2024
 */
@Data
@Entity
@Table(name = "caitemorg")
@IdClass(CaitemorgPK.class)
@EqualsAndHashCode(callSuper = true)
public class Caitemorg extends AbstractModel<String> {

	private static final long serialVersionUID = 1248748191150759806L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private Integer xitem;

	@Id
	@Basic(optional = false)
	@Column(name = "xorg")
	private Integer xorg;

	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private String orgName;
	@Transient
	private String orgType;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Caitemorg getMD13DefaultInstance(Integer xitem) {
		Caitemorg obj = new Caitemorg();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXitem(xitem);
		obj.setZactive(Boolean.TRUE);
		return obj;
	}
}
