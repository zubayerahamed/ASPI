package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XfavouritesPK;

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
@Table(name = "xfavourites")
@IdClass(XfavouritesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xfavourites extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

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

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xtype", length = 10)
	private String xtype;

	@Column(name = "xsequence")
	private Integer xsequence;

	@Column(name = "xisdefault", length = 1)
	private Boolean xisdefault;

	@Transient
	private String screenName;

}
