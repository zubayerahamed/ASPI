package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpreqdetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "opreqdetail")
@IdClass(OpreqdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opreqdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 3627232563000802304L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdoreqnum")
	private Integer xdoreqnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 25)
	private String xunit;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xrated")
	private BigDecimal xrated;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

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

	public static Opreqdetail getSO12DefaultInstance(Integer xdoreqnum) {
		Opreqdetail obj = new Opreqdetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdoreqnum(xdoreqnum);
		obj.setXrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}

	public static Opreqdetail getSO11DefaultInstance(Integer xdoreqnum) {
		Opreqdetail obj = new Opreqdetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdoreqnum(xdoreqnum);
		obj.setXrow(0);
		obj.setXrated(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}
}
