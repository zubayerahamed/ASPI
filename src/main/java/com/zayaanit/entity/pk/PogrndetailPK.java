package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PogrndetailPK implements Serializable {
    
     private Integer zid;
    private Integer xgrnnum;
    private Integer xrow;
}
