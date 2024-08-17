package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpdodetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "opdodetail")
@IdClass(OpdodetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opdodetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1010445378206070472L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private Integer xdornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 10)
	private String xunit;

	@Column(name = "xqtyreq")
	private BigDecimal xqtyreq;

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xrated")
	private BigDecimal xrated;

	@Column(name = "xdiscpreq")
	private BigDecimal xdiscpreq;

	@Column(name = "xdiscpord")
	private BigDecimal xdiscpord;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xqtycrn")
	private BigDecimal xqtycrn;

	@Column(name = "xratecost")
	private BigDecimal xratecost;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opdodetail getSO17DefaultInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdornum(xdornum);
		obj.setXrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}

	public static Opdodetail getSO18DefaultInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdornum(xdornum);
		obj.setXdocrow(0);
		obj.setXrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}

	public static Opdodetail getSO23DefaultInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdornum(xdornum);
		obj.setXdocrow(0);
		obj.setXrow(0);
		obj.setXqtyreq(BigDecimal.ZERO);
		obj.setXqtyord(BigDecimal.ZERO);
		obj.setXqtycrn(BigDecimal.ZERO);
		obj.setXratecost(BigDecimal.ZERO);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscpreq(BigDecimal.ZERO);
		return obj;
	}

	public static Opdodetail getAD17DefaultInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdornum(xdornum);
		obj.setXdocrow(0);
		obj.setXrow(0);
		obj.setXqtyreq(BigDecimal.ZERO);
		obj.setXqtyord(BigDecimal.ZERO);
		obj.setXqty(BigDecimal.ONE);
		obj.setXqtycrn(BigDecimal.ZERO);
		obj.setXratecost(BigDecimal.ZERO);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		obj.setXdiscpreq(BigDecimal.ZERO);
		return obj;
	}
}
