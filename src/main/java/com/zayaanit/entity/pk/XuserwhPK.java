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
public class XuserwhPK implements Serializable {

	private static final long serialVersionUID = 7591661967287472920L;

	private Integer zid;
	private String zemail;
	private Integer xwh;
}
