package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import com.zayaanit.entity.pk.CabankPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "cabank")
@IdClass(CabankPK.class)
@EqualsAndHashCode(callSuper = true)
public class Cabank extends AbstractModel<String> {

	private static final long serialVersionUID = 5658292875335449875L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbank")
	private Integer xbank;

	@NotBlank(message = "Bank name required")
	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xbranch", length = 50)
	private String xbranch;

	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xaccount", length = 50)
	private String xaccount;

	@Column(name = "xroute", length = 100)
	private String xroute;

	@Column(name = "xcodegl", length = 25)
	private String xcodegl;

	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private String customer;

	@Transient
	private String bank;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Cabank getDefaultInstance() {
		Cabank obj = new Cabank();
		obj.setZactive(Boolean.TRUE);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
