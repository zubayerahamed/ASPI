package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModetailPK implements Serializable {
    
    private Integer zid;
    private Integer xbatch;
    private Integer xrow;
}