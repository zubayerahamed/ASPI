package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
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
	@Column(name = "zorg", length = 50)
	private String zorg;

	@Column(name = "xphone", length = 50)
	private String xphone;

	@NotBlank
	@Column(name = "zname", length = 50)
	private String zname;

	@Column(name = "xemail", length = 50)
	private String xemail;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "zpassword", length = 25)
	private String zpassword;

	@Lob
	@Column(name = "xlogo")
	private byte[] xlogo;

	@Column(name = "xfilesize")
	private Integer xfilesize;

	@Column(name = "xdocpath", length = 100)
	private String xdocpath;

	@Column(name = "xdoctypes", length = 200)
	private String xdoctypes;

	@Column(name = "zactive")
	private Boolean zactive;

	@Min(value = 0, message = "Additional invoice allowed days must be greater or equal 0")
	@Column(name = "xaddidelays")
	private Integer xaddidelays;

	@Column(name = "xrptdefautl", length = 100)
	private String xrptdefautl;

	@Column(name = "xrptpath", length = 100)
	private String xrptpath;

	@Column(name = "xleddays")
	private Integer xleddays;

	@Transient
	private String imageBase64;

	@Transient
	private String zemail;

	@Transient
	private String xpassword;
}
