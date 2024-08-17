package com.zayaanit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.zayaanit.entity.pk.AreaCustomerViewPK;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2023
 */
@Entity
@Immutable
@IdClass(AreaCustomerViewPK.class)
@Table(name = "AreaCustomerView")
@Data
public class AreaCustomerView {

	@Id
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Column(name = "xcus")
	private Integer xcus;

	@Id
	@Column(name = "xarea")
	private Integer xarea;
}
