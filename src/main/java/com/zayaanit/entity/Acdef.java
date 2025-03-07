package com.zayaanit.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.AcdefPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acdef")
@IdClass(AcdefPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acdef extends AbstractModel<String> {

	private static final long serialVersionUID = -5040572499275380825L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "xoffset")
	private Integer xoffset;

	@Column(name = "xaccpl")
	private Integer xaccpl;

	@Column(name = "xclyear")
	private Integer xclyear;

	@Temporal(TemporalType.DATE)
	@Column(name = "xcldate")
	private Date xcldate;

	@Column(name = "xaccmc")
	private Integer xaccmc;

	@Transient
	private String accountName;

	@Transient
	private String costAccountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acdef getDefaultInstance() {
		Acdef obj = new Acdef();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}

}
