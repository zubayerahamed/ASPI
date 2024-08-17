package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OporddetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Oporddetail extends AbstractModel<String> {

	private static final long serialVersionUID = -5744393192188266448L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private Integer xordernum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 25)
	private String xunit;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xqtyreq")
	private BigDecimal xqtyreq;

	@Column(name = "xrated")
	private BigDecimal xrated;

	@Column(name = "xdiscpreq")
	private BigDecimal xdiscpreq;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xqtydel")
	private BigDecimal xqtydel;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Oporddetail getSO14DefaultInstance(Integer xdoreqnum) {
		Oporddetail obj = new Oporddetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXordernum(xdoreqnum);
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}

	public static Oporddetail getSO15DefaultInstance(Integer xdoreqnum) {
		Oporddetail obj = new Oporddetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXordernum(xdoreqnum);
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}
}
