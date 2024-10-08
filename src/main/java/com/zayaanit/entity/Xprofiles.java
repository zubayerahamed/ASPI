package com.zayaanit.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Dec 1, 2020
 */
@Data
@Entity
@Table(name = "xprofiles")
@IdClass(XprofilesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xprofiles extends AbstractModel<String> {

	private static final long serialVersionUID = 2616243655037864169L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Column(name = "xnote", length = 100)
	private String xnote;

	@Transient
	private List<Xprofilesdt> details = new ArrayList<>();

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xprofiles getDefaultInstance() {
		Xprofiles obj = new Xprofiles();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
