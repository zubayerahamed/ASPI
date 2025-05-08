package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XscreendetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since May 8, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xscreendetail")
@IdClass(XscreendetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xscreendetail extends AbstractModel<String> {

	private static final long serialVersionUID = -7051210656876929495L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow", length = 10)
	private Integer xrow;

	@Column(name = "xtype", length = 20)
	private String xtype;

	@Column(name = "xseqn")
	private Integer xseqn;

	@Column(name = "xlabel", length = 50)
	private String xlabel;

	@Column(name = "xisdisable")
	private Boolean xisdisable;

	@Column(name = "xisrequired")
	private Boolean xisrequired;

	@Column(name = "xisstartdate")
	private Boolean xisstartdate;

	@Column(name = "xisenddate")
	private Boolean xisenddate;

	@Column(name = "xmin")
	private BigDecimal xmin;

	@Column(name = "xmax")
	private BigDecimal xmax;

	@Column(name = "xstep")
	private Integer xstep;

	@Column(name = "xdefaultvalue", length = 50)
	private String xdefaultvalue;

	@Column(name = "xoptionsquery", length = 200)
	private String xoptionsquery;

	@Column(name = "xoptions", length = 200)
	private String xoptions;

	@Column(name = "xsearchcode", length = 10)
	private String xsearchcode;

	@Column(name = "xsearchsuffix", length = 10)
	private String xsearchsuffix;

	@Column(name = "xdependentfieldid", length = 10)
	private String xdependentfieldid;

	@Column(name = "xresetfieldid", length = 10)
	private String xresetfieldid;

	@Column(name = "xparamtype", length = 20)
	private String xparamtype;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xscreendetail getDefaultInstance(String xscreen) {
		Xscreendetail obj = new Xscreendetail();
		obj.setXrow(0);
		obj.setXscreen(xscreen);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
