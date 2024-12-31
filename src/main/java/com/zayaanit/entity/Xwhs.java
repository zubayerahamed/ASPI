package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xwhs")
@IdClass(XwhsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xwhs extends AbstractModel<String> {

	private static final long serialVersionUID = 3403841971090795101L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xlocation", length = 100)
	private String xlocation;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xwhs getDefaultInstance() {
		Xwhs obj = new Xwhs();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
