package com.zayaanit.entity;

import java.math.BigDecimal;
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

import com.zayaanit.entity.pk.ImtrnPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@Entity
@Table(name = "imtrn")
@IdClass(ImtrnPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtrn extends AbstractModel<String> {

	private static final long serialVersionUID = -4940556289143304573L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "ximtrnnum")
	private Integer ximtrnnum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xunit", length = 10)
	private String xunit;

	@Column(name = "xqty")
	private BigDecimal xqty;

	@Column(name = "xrate")
	private BigDecimal xrate;

	@Column(name = "xval")
	private BigDecimal xval;

	@Column(name = "xsign")
	private Integer xsign;

	@Column(name = "xrateavg")
	private BigDecimal xrateavg;

	@Column(name = "xcqtyuse")
	private BigDecimal xcqtyuse;
	
	@Column(name = "xdocnum")
	private Integer xdocnum;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xdoctype", length = 25)
	private String xdoctype;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xorigin", length = 10)
	private String xorigin;

}
