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
public class OpshipclnPK implements Serializable {

	private static final long serialVersionUID = 5473804737342300269L;
	private Integer zid;
	private String xtypecln;
	private Integer xdocnum;
}
