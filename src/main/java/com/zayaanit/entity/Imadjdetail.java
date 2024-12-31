package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ImadjdetailPK;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for IM Adjustment Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imadjdetail")
@IdClass(ImadjdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imadjdetail extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536135L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xadjnum")
    private Integer xadjnum;

    @Id
    @Basic(optional = false)
    @Column(name = "xrow")
    private Integer xrow;

    @Column(name = "xitem")
    private Integer xitem;

    @Column(name = "xqty", precision = 15, scale = 2)
    private BigDecimal xqty;

    @Column(name = "xsign")
    private Integer xsign;

    @Column(name = "xrate", precision = 15, scale = 2)
    private BigDecimal xrate;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xnote", length = 200)
    private String xnote;

    public static Imadjdetail getDefaultInstance() {
        Imadjdetail obj = new Imadjdetail();
        // Set default values if needed
        return obj;
    }
}
