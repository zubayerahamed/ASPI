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
public class ImadjdetailPK implements Serializable {

	private static final long serialVersionUID = 6503024111440093579L;

	private Integer zid;
	private Integer xadjnum;
	private Integer xrow;
}
