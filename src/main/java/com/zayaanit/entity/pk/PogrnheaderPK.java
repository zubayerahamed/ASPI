package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PogrnheaderPK implements Serializable {

	private static final long serialVersionUID = 5082945769961600404L;

	private Integer zid;
	private Integer xgrnnum;
}
