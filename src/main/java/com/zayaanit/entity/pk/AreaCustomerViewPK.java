package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 26, 2023
 */
@Data
public class AreaCustomerViewPK implements Serializable {

	private static final long serialVersionUID = 3661231473602823526L;
	private Integer zid;
	private Integer xcus;
	private Integer xarea;
}
