package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.TermstrnPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@Entity
@Table(name = "termstrn")
@IdClass(TermstrnPK.class)
@EqualsAndHashCode(callSuper = true)
public class Termstrn extends AbstractModel<String> {

	private static final long serialVersionUID = -6498816727166582427L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Id
	@Basic(optional = false)
	@Column(name = "xdocnum")
	private Integer xdocnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xterm", length = 100)
	private String xterm;

	@Column(name = "xdesc", length = 200)
	private String xdesc;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xserial")
	private Integer xserial;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Termstrn getSO10DefaultInstance(Integer xdocnum) {
		Termstrn obj = new Termstrn();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdocnum(xdocnum);
		obj.setXscreen("SO10");
		return obj;
	}
}
