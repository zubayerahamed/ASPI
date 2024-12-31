package com.zayaanit.entity.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotogliPK implements Serializable {
    
    private Integer zid;
    private String xtype;
    private String xgsup;
    private String xgitem;
}
