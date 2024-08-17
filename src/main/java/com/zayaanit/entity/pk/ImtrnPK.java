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
@NoArgsConstructor
@AllArgsConstructor
public class ImtrnPK implements Serializable {

	private static final long serialVersionUID = 6941015394661336160L;
	private Integer zid;
	private Integer ximtrnnum;
}
