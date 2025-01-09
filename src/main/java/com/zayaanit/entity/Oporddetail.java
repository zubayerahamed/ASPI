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
 * Entity for Order Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Oporddetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536085L;

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

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtydel", precision = 15, scale = 2)
	private BigDecimal xqtydel;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Oporddetail getDefaultInstance(Integer xordernum) {
		Oporddetail obj = new Oporddetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXordernum(xordernum);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXqtydel(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
