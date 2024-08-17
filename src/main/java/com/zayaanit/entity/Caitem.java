package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "caitem")
@IdClass(CaitemPK.class)
@EqualsAndHashCode(callSuper = true)
public class Caitem extends AbstractModel<String> {

	private static final long serialVersionUID = -4579492106206759411L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xcatitem", length = 100)
	private String xcatitem;

	@Column(name = "xunit", length = 10)
	private String xunit;

	@Column(name = "xctype", length = 25)
	private String xctype;

	@Column(name = "xcost")
	private BigDecimal xcost;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xspec", length = 100)
	private String xspec;

	@Column(name = "xcodeold", length = 25)
	private String xcodeold;

	@Column(name = "xtype", length = 10)
	private String xtype;

	@Column(name = "xgitem", length = 100)
	private String xgitem;

	@Column(name = "xsubcat", length = 100)
	private String xsubcat;

	@Column(name = "xpack", length = 100)
	private String xpack;

	@Column(name = "xminqty")
	private BigDecimal xminqty;

	@Transient
	private Integer xwh;
	@Transient
	private String store;
	@Transient
	private BigDecimal xqty;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Caitem getDefaultInstance() {
		Caitem obj = new Caitem();
		obj.setXctype("Weighted Average");
		obj.setXcost(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXtype("Item");
		obj.setXminqty(BigDecimal.ZERO);
		return obj;
	}
}
