package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.AcmstPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acmst")
@IdClass(AcmstPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acmst extends AbstractModel<String> {

	private static final long serialVersionUID = -2283958940588239713L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xgroup")
	private Integer xgroup;

	@Column(name = "xorg", length = 100)
	private String xdesc;

	@Column(name = "xacctype", length = 50)
	private String xacctype;

	@Column(name = "xaccusage", length = 50)
	private String xaccusage;

}
