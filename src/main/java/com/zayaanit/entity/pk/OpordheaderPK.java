package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpordheaderPK implements Serializable {

	private static final long serialVersionUID = 1002382703828739893L;

	private Integer zid;
	private Integer xordernum;
}
