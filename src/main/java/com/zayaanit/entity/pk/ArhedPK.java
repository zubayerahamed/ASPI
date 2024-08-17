package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * Jul 2, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArhedPK implements Serializable{

	private static final long serialVersionUID = -2654772305376650382L;

	private Integer zid;
	private Integer xtrnnum;
	private String xscreen;
}
