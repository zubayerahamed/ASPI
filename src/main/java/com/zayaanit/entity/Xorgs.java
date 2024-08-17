package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2024
 */
@Data
@Entity
@Table(name = "xorgs")
@IdClass(XorgsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xorgs extends AbstractModel<String> {

	private static final long serialVersionUID = -3478721125027981066L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xorg", length = 20)
	private Integer xorg;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xname", length = 100)
	private String xname;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xorgs getDefaultInstance() {
		Xorgs obj = new Xorgs();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXtype("Sale");
		return obj;
	}
}
