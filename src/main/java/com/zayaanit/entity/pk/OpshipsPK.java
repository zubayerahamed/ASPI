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
public class OpshipsPK implements Serializable {

	private static final long serialVersionUID = -747316910051180761L;

	private Integer zid;
	private Integer xshipment;
}
