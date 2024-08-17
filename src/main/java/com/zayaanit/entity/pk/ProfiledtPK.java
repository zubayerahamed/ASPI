package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Zubayer Ahamed Jul 2, 2023
 */
@Data
public class ProfiledtPK implements Serializable {

	private static final long serialVersionUID = -1542794175138185880L;

	private Integer zid;
	private String xprofile;
	private String xscreen;
}
