package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.ImrcvdetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "imrcvdetail")
@IdClass(ImrcvdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imrcvdetail  extends AbstractModel<String> {

	private static final long serialVersionUID = -5798158771315212838L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrcvnum")
	private Integer xrcvnum;

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

	public static Imrcvdetail getIM12DefaultInstance(Integer xrcvnum) {
		Imrcvdetail obj = new Imrcvdetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrcvnum(xrcvnum);
		obj.setXrow(0);
		obj.setXrate(BigDecimal.ONE);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
