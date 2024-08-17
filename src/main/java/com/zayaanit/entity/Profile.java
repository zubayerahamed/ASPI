package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.zayaanit.entity.pk.ProfilePK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Data
@Entity
@Table(name = "profile")
@IdClass(ProfilePK.class)
@EqualsAndHashCode(callSuper = true)
public class Profile extends AbstractModel<String> {

	private static final long serialVersionUID = 2616243655037864169L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xprofile", length = 20)
	private String xprofile;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Profile getDefaultInstance() {
		Profile obj = new Profile();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
