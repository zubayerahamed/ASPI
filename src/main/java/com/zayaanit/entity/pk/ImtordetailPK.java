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
@NoArgsConstructor
@AllArgsConstructor
public class ImtordetailPK implements Serializable {

	private static final long serialVersionUID = 6067619219650934756L;
	private Integer zid;
	private Integer xtornum;
	private Integer xrow;

}
