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
public class OpdoheaderPK implements Serializable {

	private static final long serialVersionUID = 7170642612530042605L;
	private Integer zid;
	private Integer xdornum;
}
