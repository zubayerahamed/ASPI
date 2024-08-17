package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpvhlsPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 14, 2024
 */
@Data
@Entity
@Table(name = "opvhls")
@IdClass(OpvhlsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opvhls extends AbstractModel<String> {

	private static final long serialVersionUID = -4161023473685937138L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvhl")
	private Integer xvhl;

	@Column(name = "xlicence", length = 50)
	private String xlicence;

	@Column(name = "xtypeowner", length = 100)
	private String xtypeowner;

	@Column(name = "xdriver", length = 100)
	private String xdriver;

	@Column(name = "xcompany", length = 100)
	private String xcompany;

	@Column(name = "xtypevhl", length = 25)
	private String xtypevhl;

	@Column(name = "xcapton")
	private BigDecimal xcapton;

	@Column(name = "xcapfeet")
	private BigDecimal xcapfeet;

	@Column(name = "xcontact", length = 100)
	private String xcontact;

	@Column(name = "xnid", length = 100)
	private String xnid;

	@Column(name = "xmobile", length = 50)
	private String xmobile;

	@Column(name = "xtradlic", length = 50)
	private String xtradlic;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opvhls getDefaultInstance() {
		Opvhls obj = new Opvhls();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXcapton(BigDecimal.ZERO);
		obj.setXcapfeet(BigDecimal.ZERO);
		return obj;
	}
}
