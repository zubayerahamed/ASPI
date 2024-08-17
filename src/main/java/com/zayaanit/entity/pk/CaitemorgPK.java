package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jan 13, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaitemorgPK implements Serializable {

	private static final long serialVersionUID = -4940534745114362376L;

	private Integer zid;
	private Integer xitem;
	private Integer xorg;
}
