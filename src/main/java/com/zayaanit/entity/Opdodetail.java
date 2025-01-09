package com.zayaanit.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.OpdodetailPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for DO Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "opdodetail")
@IdClass(OpdodetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opdodetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536092L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private Integer xdornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqtyord", precision = 15, scale = 2)
	private BigDecimal xqtyord;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtycrn", precision = 15, scale = 2)
	private BigDecimal xqtycrn;

	@Column(name = "xrategrn", precision = 15, scale = 2)
	private BigDecimal xrategrn;

	@Column(name = "xnote", length = 200)
	private String xnote;

	public static Opdodetail getDefaultInstance() {
		Opdodetail obj = new Opdodetail();
		// Set default values if needed
		return obj;
	}
}
