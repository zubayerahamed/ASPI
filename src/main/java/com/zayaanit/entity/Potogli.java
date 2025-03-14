package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.PotogliPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Potogli.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "potogli")
@IdClass(PotogliPK.class)
@EqualsAndHashCode(callSuper = true)
public class Potogli extends AbstractModel<String> {

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
	@Column(name = "xgsup", length = 25)
	private String xgsup;

	@Id
	@Basic(optional = false)
	@Column(name = "xgitem", length = 25)
	private String xgitem;

	@Column(name = "xaccdr")
	private Integer xaccdr;

	@Column(name = "xacccr")
	private Integer xacccr;

	@Column(name = "xaccadj")
	private Integer xaccadj;

	@Transient
	private String debitAccount;
	@Transient
	private String creditAccount;
	@Transient
	private String adjustmentAccount;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Potogli getDefaultInstance() {
		Potogli obj = new Potogli();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
