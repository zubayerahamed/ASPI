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
public class ImissuedetailPK implements Serializable {

	private static final long serialVersionUID = 2096827627469783898L;
	private Integer zid;
	private Integer xissuenum;
	private Integer xrow;
}
