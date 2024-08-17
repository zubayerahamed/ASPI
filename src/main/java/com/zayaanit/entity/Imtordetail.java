package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.ImtordetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "imtordetail")
@IdClass(ImtordetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtordetail extends AbstractModel<String> {

	private static final long serialVersionUID = -7331674660116909451L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private Integer xtornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 10)
	private String xunit;

	@Column(name = "xqtyreq")
	private BigDecimal xqtyreq;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imtordetail getIM11DefaultInstance(Integer xtornum) {
		Imtordetail obj = new Imtordetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXtornum(xtornum);
		obj.setXrow(0);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}

	public static Imtordetail getIM16DefaultInstance(Integer xtornum) {
		Imtordetail obj = new Imtordetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXtornum(xtornum);
		obj.setXrow(0);
		obj.setXqtyreq(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
