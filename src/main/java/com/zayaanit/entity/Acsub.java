package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.AcsubPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acsub")
@IdClass(AcsubPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acsub extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536070L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private Integer xacc;

	@Id
	@Basic(optional = false)
	@Column(name = "xsub")
	private Integer xsub;

	@Column(name = "xorg", length = 100)
	private String xdesc;
}
