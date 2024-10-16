package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.AcgroupPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acgroup")
@IdClass(AcgroupPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acgroup extends AbstractModel<String> {

	private static final long serialVersionUID = 5563603837100413499L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xagcode")
	private Integer xagcode;

	@Column(name = "xagname", length = 50)
	private String xagname;

	@Column(name = "xaglevel")
	private Integer xaglevel;

	@Column(name = "xagparent")
	private Integer xagparent;

	@Column(name = "xagtype", length = 50)
	private String xagtype;

	@Transient
	private Integer againParent;

	@Transient
	private String parentName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acgroup getDefaultInstance() {
		Acgroup obj = new Acgroup();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXaglevel(1);
		return obj;
	}
}
