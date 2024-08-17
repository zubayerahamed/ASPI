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
public class ImrcvdetailPK implements Serializable {

	private static final long serialVersionUID = -6005569910497777473L;
	private Integer zid;
	private Integer xrcvnum;
	private Integer xrow;
}
