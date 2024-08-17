package com.zayaanit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jan 21, 2024
 */
@Data
@Entity
@Table(name = "adlogs")
@EqualsAndHashCode(callSuper = true)
public class Adlogs extends AbstractModel<String> {

	private static final long serialVersionUID = -641890194499849265L;

	private Integer zid;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "xlogid")
	private Long xlogid;

	@Column(name = "xlogtype", length = 50)
	private String xlogtype;

	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xdetail", length = 200)
	private String xdetail;

	@Column(name = "xstatus", length = 200)
	private String xstatus;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xdocnum")
	private Integer xdocnum;
}
