package com.zayaanit.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zayaanit.entity.pk.XlogsPK;

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
@Table(name = "xlogs")
@IdClass(XlogsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xlogs extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsession")
	private String xsession;

	@Column(name = "xsip")
	private String xsip;

	@Column(name = "xcip")
	private String xcip;

	@Column(name = "zemail")
	private String zemail;

	@Column(name = "xprofile")
	private String xprofile;

	@Column(name = "xstaff")
	private String xstaff;

	@Column(name = "xuseragent")
	private String xuseragent;

	@Column(name = "xlogintime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xlogintime;

	@Column(name = "xlogouttime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xlogouttime;
}
