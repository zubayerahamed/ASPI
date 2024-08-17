package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ProfiledtPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Data
@Entity
@Table(name = "profiledt")
@IdClass(ProfiledtPK.class)
@EqualsAndHashCode(callSuper = true)
public class Profiledt extends AbstractModel<String> {

	private static final long serialVersionUID = -9011140154721641297L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 20)
	private String xprofile;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;
}
