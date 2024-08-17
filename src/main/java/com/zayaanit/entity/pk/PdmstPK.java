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
public class PdmstPK implements Serializable {

	static final long serialVersionUID = -3129346555301341107L;

	private Integer zid;
	private Integer xstaff;
}
