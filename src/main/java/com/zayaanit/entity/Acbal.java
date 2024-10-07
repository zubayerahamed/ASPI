package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.AcbalPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acbal")
@IdClass(AcbalPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "FA_YearEnd", procedureName = "FA_YearEnd", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "year", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "date", type = Date.class)
	})
})
public class Acbal extends AbstractModel<String> {

	private static final long serialVersionUID = -1093692323942299487L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xsub")
	private Integer xsub;

	@Column(name = "xprime")
	private BigDecimal xprime;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xper")
	private Integer xper;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xhnote", length = 200)
	private String xhnote;

	@Column(name = "xdnote", length = 200)
	private String xdnote;

}
