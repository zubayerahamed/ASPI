package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.CadocPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 6, 2023
 */
@Data
@Entity
@Table(name = "cadoc")
@IdClass(CadocPK.class)
@EqualsAndHashCode(callSuper = true)
public class Cadoc extends AbstractModel<String> {

	private static final long serialVersionUID = 2292369485864274354L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdocid")
	private Integer xdocid;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xtrnnum")
	private Integer xtrnnum;

	@Column(name = "xtitle", length = 100)
	private String xtitle;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xdoctype", length = 10)
	private String xdoctype;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xnameold", length = 100)
	private String xnameold;

	@Column(name = "xext", length = 10)
	private String xext;

	@Column(name = "xmmyyyy", length = 10)
	private String xmmyyyy;

}
