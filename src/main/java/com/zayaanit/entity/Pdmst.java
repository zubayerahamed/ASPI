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
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "pdmst")
@IdClass(PdmstPK.class)
@EqualsAndHashCode(callSuper = true)
public class Pdmst extends AbstractModel<String> {

	private static final long serialVersionUID = -471484890556386538L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xstaff")
	private Integer xstaff;

	@NotBlank
	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xdesignation", length = 100)
	private String xdesignation;

	@Column(name = "xemail", length = 100)
	private String xemail;

	@Column(name = "xdepartment", length = 100)
	private String xdepartment;

	@Column(name = "xmobile", length = 11)
	private String xmobile;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdoj")
	private Date xdoj;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdol")
	private Date xdol;

	@Column(name = "xstatusemp", length = 50)
	private String xstatusemp;

	@Column(name = "xarea")
	private Integer xarea;

	@Transient
	private String area;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Pdmst getDefaultInstance() {
		Pdmst obj = new Pdmst();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
