package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ImissuedetailPK;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for IM ISSUE Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imissuedetail")
@IdClass(ImissuedetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imissuedetail extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536122L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xissuenum")
    private Integer xissuenum;

    @Id
    @Basic(optional = false)
    @Column(name = "xrow")
    private Integer xrow;

    @Column(name = "xitem")
    private Integer xitem;

    @Column(name = "xqty", precision = 15, scale = 2)
    private BigDecimal xqty;

    @Column(name = "xrate", precision = 15, scale = 2)
    private BigDecimal xrate;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xnote", length = 200)
    private String xnote;

    public static Imissuedetail getDefaultInstance() {
        Imissuedetail obj = new Imissuedetail();
        // Set default values if needed
        return obj;
    }
}
