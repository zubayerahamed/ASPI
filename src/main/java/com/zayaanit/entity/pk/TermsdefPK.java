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
public class TermsdefPK implements Serializable {

	private static final long serialVersionUID = 7318457597790561732L;

	private Integer zid;
	private String xtype;
	private String xterm;
}
