package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ModetailPK;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for MO Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "modetail")
@IdClass(ModetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Modetail extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536139L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xbatch")
    private Integer xbatch;

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

    @Column(name = "xsign")
    private Integer xsign;

    @Column(name = "xtype", length = 25)
    private String xtype;

    @Column(name = "xnote", length = 200)
    private String xnote;

    public static Modetail getDefaultInstance() {
        Modetail obj = new Modetail();
        // Set default values if needed
        return obj;
    }
}
