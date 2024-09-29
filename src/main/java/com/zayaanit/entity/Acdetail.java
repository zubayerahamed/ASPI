package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.AcdetailPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acdetail")
@IdClass(AcdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acdetail extends AbstractModel<String> {

	private static final long serialVersionUID = -1093692323942299487L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xsub")
	private Integer xsub;

	@Column(name = "xprime")
	private BigDecimal xprime;

	@Column(name = "xdebit")
	private BigDecimal xdebit;

	@Column(name = "xcredit")
	private BigDecimal xcredit;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acdetail getDefaultInstance(Integer xvoucher) {
		Acdetail obj = new Acdetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXvoucher(xvoucher);
		return obj;
	}
}
