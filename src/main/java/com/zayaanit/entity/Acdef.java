package com.zayaanit.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.AcdefPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acdef")
@IdClass(AcdefPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acdef extends AbstractModel<String> {

	private static final long serialVersionUID = -5040572499275380825L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "xoffset")
	private Integer xoffset;

	@Column(name = "xaccpl")
	private Integer xaccpl;

	@Column(name = "xbacklock", length = 25)
	private String xbacklock;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatelock")
	private Date xdatelock;

	@Column(name = "xgateyp", length = 25)
	private String xgateyp;

	@Column(name = "xdefaultyear")
	private Integer xdefaultyear;

	@Column(name = "xdefaultper")
	private Integer xdefaultper;

}
