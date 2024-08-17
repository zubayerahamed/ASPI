package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "oparea")
@IdClass(OpareaPK.class)
@EqualsAndHashCode(callSuper = true)
public class Oparea extends AbstractModel<String> {

	private static final long serialVersionUID = -1332880538875236705L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xarea")
	private Integer xarea;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xclass", length = 25)
	private String xclass;

	@Column(name = "xmobile", length = 100)
	private String xmobile;

	@Column(name = "xemail", length = 100)
	private String xemail;

	@Column(name = "xparea")
	private Integer xparea;

	@Transient
	private String parentAreaName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Oparea getDefaultInstance() {
		Oparea obj = new Oparea();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
