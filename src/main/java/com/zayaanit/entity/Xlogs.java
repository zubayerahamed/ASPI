package com.zayaanit.entity;

import java.io.Serializable;
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
public class Xlogs implements Serializable {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsession", length = 100)
	private String xsession;

	@Column(name = "xsip", length = 50)
	private String xsip;

	@Column(name = "xcip", length = 50)
	private String xcip;

	@Column(name = "zemail", length = 25)
	private String zemail;

	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Column(name = "xstaff", length = 25)
	private String xstaff;

	@Column(name = "xuseragent", length = 255)
	private String xuseragent;

	@Column(name = "xexptype", length = 25)
	private String xexptype;

	@Column(name = "xlogintime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xlogintime;

	@Column(name = "xlogouttime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xlogouttime;
}
