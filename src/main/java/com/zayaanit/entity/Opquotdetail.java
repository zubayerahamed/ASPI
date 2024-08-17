package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpquotdetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@Entity
@Table(name = "opquotdetail")
@IdClass(OpquotdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opquotdetail extends AbstractModel<String> {

	private static final long serialVersionUID = -7807425190806319865L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xquotnum")
	private Integer xquotnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 10)
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

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opquotdetail getSO10DefaultInstance(Integer xquotnum) {
		Opquotdetail obj = new Opquotdetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXquotnum(xquotnum);
		obj.setXrow(0);

		obj.setXqty(BigDecimal.ONE);
		obj.setXrated(BigDecimal.ONE);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscp(BigDecimal.ZERO);
		return obj;
	}
}
