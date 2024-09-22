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

import com.zayaanit.entity.pk.AcheaderPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acheader")
@IdClass(AcheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4696242623656256597L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xvtype", length = 25)
	private String xvtype;

	@Column(name = "xcheque", length = 25)
	private String xcheque;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xper")
	private Integer xper;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xstaff")
	private Integer xstaff;

}
