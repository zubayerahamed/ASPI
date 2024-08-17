package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.converter.CKTimeConverter;
import com.zayaanit.entity.pk.OpshipclnPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.util.CKTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Apr 14, 2024
 */
@Data
@Entity
@Table(name = "opshipcln")
@IdClass(OpshipclnPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opshipcln extends AbstractModel<String> {

	private static final long serialVersionUID = -5010004686717350069L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtypecln", length = 25)
	private String xtypecln;

	@Id
	@Basic(optional = false)
	@Column(name = "xdocnum")
	private Integer xdocnum;

	@Column(name = "xshipment")
	private Integer xshipment;

	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xpointd")
	private Integer xpointd;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatedoc")
	private Date xdatedoc;

	@Column(name = "xamtdoc")
	private BigDecimal xamtdoc;

	@Column(name = "xlocation", length = 100)
	private String xlocation;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdatedel")
	private Date xdatedel;

	@Column(name = "xpointc")
	private Integer xpointc;

	@Column(name = "xdetails", length = 100)
	private String xdetails;

	@Column(name = "xrcvby", length = 100)
	private String xrcvby;
	
	@Column(name = "xrcbmobile", length = 100)
	private String xrcbmobile;

	@Transient
	@Convert(converter = CKTimeConverter.class)
	private CKTime xdatedeltime;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opshipcln getDefaultInstance(Integer xshipment) {
		Opshipcln obj = new Opshipcln();
		obj.setXshipment(xshipment);
		obj.setXrow(0);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
