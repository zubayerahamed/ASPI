package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.XwidgetsPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Dec 30, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xwidgets")
@IdClass(XwidgetsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xwidgets extends AbstractModel<String> {

	private static final long serialVersionUID = 4422439439094620583L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwidget", length = 10)
	private String xwidget;

	@Column(name = "xtitle", length = 50)
	private String xtitle;
}
