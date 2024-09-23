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

import com.zayaanit.entity.pk.XlogsdtPK;

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
@Table(name = "xlogsdt")
@IdClass(XlogsdtPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xlogsdt extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsession")
	private String xsession;

	@Column(name = "xdatetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date xdatetime;

	@Column(name = "xscreen")
	private String xscreen;

	@Column(name = "xfunc")
	private String xfunc;

	@Column(name = "xsource")
	private String xsource;

	@Column(name = "xfuncdt")
	private String xfuncdt;

	@Column(name = "xdata")
	private String xdata;

	@Column(name = "xstatement")
	private String xstatement;

	@Column(name = "xmessage")
	private String xmessage;

	@Column(name = "xresult")
	private String xresult;

}
