package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.TermsdefPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@Entity
@Table(name = "termsdef")
@IdClass(TermsdefPK.class)
@EqualsAndHashCode(callSuper = true)
public class Termsdef extends AbstractModel<String> {

	private static final long serialVersionUID = -2968490169525708254L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype", length = 25)
	private String xtype;

	@Id
	@Basic(optional = false)
	@Column(name = "xterm", length = 100)
	private String xterm;

	@Column(name = "xdesc", length = 200)
	private String xdesc;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xserial")
	private Integer xserial;

	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Termsdef getDefaultInstance() {
		Termsdef obj = new Termsdef();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.TRUE);
		obj.setXserial(0);
		return obj;
	}
}
