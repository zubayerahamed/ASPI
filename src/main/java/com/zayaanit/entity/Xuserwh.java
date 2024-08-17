package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XuserwhPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2024
 */
@Data
@Entity
@Table(name = "xuserwh")
@IdClass(XuserwhPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xuserwh extends AbstractModel<String> {

	private static final long serialVersionUID = 1991034767163485586L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 20)
	private String zemail;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xisprime", length = 1)
	private Boolean xisprime = Boolean.FALSE;

	@Transient
	private String store;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xuserwh getAD13DefaultInstance(String zemail) {
		Xuserwh obj = new Xuserwh();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZemail(zemail);
		obj.setXisprime(false);
		return obj;
	}
}
