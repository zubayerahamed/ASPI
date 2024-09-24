package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XmenusPK;
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
@Table(name = "xmenus")
@IdClass(XmenusPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xmenus extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xmenu", length = 10)
	private String xmenu;

	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xpmenu", length = 10)
	private String xpmenu;

	@Column(name = "xicon", length = 50)
	private String xicon;

	@Column(name = "xsequence")
	private Integer xsequence;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xmenus getDefaultInstance() {
		Xmenus obj = new Xmenus();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
