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
public class ImissueheaderPK implements Serializable {

	private static final long serialVersionUID = 3822275448949489497L;

	private Integer zid;
	private Integer xissuenum;
}
