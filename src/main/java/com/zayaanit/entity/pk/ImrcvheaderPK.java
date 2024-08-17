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
public class ImrcvheaderPK implements Serializable {

	private static final long serialVersionUID = -4455310630484721415L;

	private Integer zid;
	private Integer xrcvnum;
}
