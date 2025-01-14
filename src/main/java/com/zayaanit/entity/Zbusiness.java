package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "zbusiness")
@EqualsAndHashCode(callSuper = true)
public class Zbusiness extends AbstractModel<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@NotBlank
	@Column(name = "zorg", length = 100)
	private String zorg;

	@Column(name = "xphone", length = 100)
	private String xphone;

	@Column(name = "xemail", length = 100)
	private String xemail;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xtin", length = 100)
	private String xtin;

	@Column(name = "xvatregno", length = 100)
	private String xvatregno;

	@Column(name = "xfilesize")
	private Integer xfilesize;

	@Column(name = "xdocpath", length = 100)
	private String xdocpath;

	@Column(name = "xdoctypes", length = 200)
	private String xdoctypes;

	@Column(name = "zactive")
	private Boolean zactive;

	@Column(name = "xisaudit")
	private Boolean xisaudit;

	@Column(name = "xsessiontime")
	private Integer xsessiontime;

	@Column(name = "xposcus")
	private Integer xposcus;

	@Column(name = "xisspeech")
	private Boolean xisspeech;

	@Lob
	@Column(name = "xlogo")
	private byte[] xlogo;

	@Column(name = "xrptdefautl", length = 100)
	private String xrptdefautl;

	@Column(name = "xrptpath", length = 100)
	private String xrptpath;

	@Transient
	private String imageBase64;

	@Transient
	private String customerName;
}
