package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acsub")
@IdClass(AcsubPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acsub extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536070L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsub")
	private Integer xsub;

	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Transient
	private String accountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acsub getDefaultInstance() {
		Acsub obj = new Acsub();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
