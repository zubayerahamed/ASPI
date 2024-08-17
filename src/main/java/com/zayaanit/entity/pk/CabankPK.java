package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed Jul 2, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabankPK implements Serializable {

	private static final long serialVersionUID = -690242734011007360L;

	private Integer zid;
	private Integer xbank;
}
