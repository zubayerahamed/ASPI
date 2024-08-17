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
@NoArgsConstructor
@AllArgsConstructor
public class XhwhsPK implements Serializable{
	private static final long serialVersionUID = 3791858550253961919L;

	private Integer zid;
	private Integer xhwh;
}
