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
public class OpcrnheaderPK implements Serializable {

	private static final long serialVersionUID = 3668049901625180638L;
	private Integer zid;
	private Integer xcrnnum;
}
