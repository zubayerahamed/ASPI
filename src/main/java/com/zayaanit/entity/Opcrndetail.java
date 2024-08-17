package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpcrndetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "opcrndetail")
@IdClass(OpcrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opcrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = -6265744936774856611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private Integer xcrnnum;

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

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xqtyinv")
	private BigDecimal xqtyinv;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xrategrn")
	private BigDecimal xrategrn;

	@Column(name = "xlineamt")
	private BigDecimal xlineamt;

	@Column(name = "xrated")
	private BigDecimal xrated;

	@Column(name = "xdiscp")
	private BigDecimal xdiscp;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xcrntype", length = 25)
	private String xcrntype;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opcrndetail getSO19DefaultInstance(Integer xcrnnum) {
		Opcrndetail obj = new Opcrndetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXcrnnum(xcrnnum);
		obj.setXrow(0);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXrated(BigDecimal.ONE);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}

	public static Opcrndetail getSO20DefaultInstance(Integer xcrnnum) {
		Opcrndetail obj = new Opcrndetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXcrnnum(xcrnnum);
		obj.setXrow(0);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXrated(BigDecimal.ONE);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}
}
