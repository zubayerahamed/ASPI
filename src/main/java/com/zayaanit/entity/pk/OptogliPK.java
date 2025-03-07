package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptogliPK implements Serializable {

	private static final long serialVersionUID = 8921870221028939994L;

	private Integer zid;
	private String xtype;
	private String xgcus;
}
