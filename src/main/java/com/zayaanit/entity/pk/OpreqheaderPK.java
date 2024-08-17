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
public class OpreqheaderPK implements Serializable {

	private static final long serialVersionUID = 8887623145008866572L;
	private Integer zid;
	private Integer xdoreqnum;
}
