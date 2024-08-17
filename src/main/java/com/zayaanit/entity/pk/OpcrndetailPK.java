package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 6, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcrndetailPK implements Serializable {

	private static final long serialVersionUID = 7951331997607788758L;
	private Integer zid;
	private Integer xcrnnum;
	private Integer xrow;
}
