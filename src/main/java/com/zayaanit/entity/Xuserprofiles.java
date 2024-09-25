package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XuserprofilesPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xuserprofiles")
@IdClass(XuserprofilesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xuserprofiles extends AbstractModel<String> {

	private static final long serialVersionUID = -1919447982690651718L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 25)
	private String zemail;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xuserprofiles getDefaultInstance(String zemail) {
		Xuserprofiles obj = new Xuserprofiles();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZemail(zemail);
		return obj;
	}
}
