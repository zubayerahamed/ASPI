package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jan 25, 2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermstrnPK implements Serializable {

	private static final long serialVersionUID = -4558205667110938042L;

	private Integer zid;
	private String xscreen;
	private Integer xdocnum;
	private String xterm;
}
