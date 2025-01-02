package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.PoorddetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Purchase Order Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "poorddetail")
@IdClass(PoorddetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Poorddetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536073L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xpornum")
	private Integer xpornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtygrn", precision = 15, scale = 2)
	private BigDecimal xqtygrn;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Poorddetail getDefaultInstance(Integer xpornum) {
		Poorddetail obj = new Poorddetail();
		obj.setXrow(0);
		obj.setXpornum(xpornum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXqtygrn(BigDecimal.ZERO);
		obj.setXlineamt(obj.getXqty().multiply(obj.getXrate()));
		return obj;
	}
}
