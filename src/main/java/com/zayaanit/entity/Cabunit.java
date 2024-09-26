package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "cabunit")
@IdClass(CabunitPK.class)
@EqualsAndHashCode(callSuper = true)
public class Cabunit extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbuid ")
	private Integer xbuid;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Cabunit getDefaultInstance() {
		Cabunit obj = new Cabunit();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
