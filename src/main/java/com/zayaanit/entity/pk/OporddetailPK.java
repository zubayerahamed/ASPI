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
public class OporddetailPK implements Serializable {

	private static final long serialVersionUID = -6130616136130577215L;
	private Integer zid;
	private Integer xordernum;
	private Integer xrow;
}
