package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpshipexpPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 14, 2024
 */
@Data
@Entity
@Table(name = "opshipexp")
@IdClass(OpshipexpPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opshipexp extends AbstractModel<String> {

	private static final long serialVersionUID = 8131380267409838699L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xshipment")
	private Integer xshipment;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xexptype", length = 25)
	private String xexptype;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opshipexp getDefaultInstance(Integer xshipment) {
		Opshipexp obj = new Opshipexp();
		obj.setXshipment(xshipment);
		obj.setXrow(0);
		obj.setXcost(BigDecimal.ZERO);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
