package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcrndetailPK implements Serializable {
    
    private Integer zid;
    private Integer xcrnnum;
    private Integer xrow;
}
