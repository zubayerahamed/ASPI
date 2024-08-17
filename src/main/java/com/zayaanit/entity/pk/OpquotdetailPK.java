package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpquotdetailPK implements Serializable {

	private static final long serialVersionUID = -8555791807292002653L;

	private Integer zid;
	private Integer xquotnum;
	private Integer xrow;
}
