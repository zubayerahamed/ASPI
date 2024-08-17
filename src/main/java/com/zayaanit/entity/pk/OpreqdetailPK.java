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
public class OpreqdetailPK implements Serializable {
	private static final long serialVersionUID = -3008466852822351099L;

	private Integer zid;
	private Integer xdoreqnum;
	private Integer xrow;
}
