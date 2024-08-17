package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Apr 14, 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpvhlsPK implements Serializable {

	private static final long serialVersionUID = 3693209747964420035L;

	private Integer zid;
	private Integer xvhl;
}
