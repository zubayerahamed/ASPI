package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Nov 30, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CasmsPK implements Serializable {

	private static final long serialVersionUID = 2095380097516022361L;

	private Integer zid;
	private String xtype;
}
