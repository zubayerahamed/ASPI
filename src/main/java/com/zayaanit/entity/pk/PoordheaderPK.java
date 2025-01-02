package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoordheaderPK implements Serializable {

	private static final long serialVersionUID = -7750714595004628092L;

	private Integer zid;
	private Integer xpornum;
}
