package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OptogliPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Optogli.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "optogli")
@IdClass(OptogliPK.class)
@EqualsAndHashCode(callSuper = true)
public class Optogli extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536143L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtype", length = 25)
	private String xtype;

	@Id
	@Basic(optional = false)
	@Column(name = "xgcus", length = 25)
	private String xgcus;

	@Column(name = "xaccdr")
	private Integer xaccdr;

	@Column(name = "xacccr")
	private Integer xacccr;

	@Column(name = "xaccdisc")
	private Integer xaccdisc;

	@Column(name = "xaccvat")
	private Integer xaccvat;

	@Column(name = "xaccait")
	private Integer xaccait;

	@Transient
	private String receivableAccount;
	@Transient
	private String salesAccount;
	@Transient
	private String discountAccount;
	@Transient
	private String vatAccount;
	@Transient
	private String aitAccount;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Optogli getDefaultInstance() {
		Optogli obj = new Optogli();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
