package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.PogrndetailPK;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for GRN Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "pogrndetail")
@IdClass(PogrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Pogrndetail extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536077L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xgrnnum")
    private Integer xgrnnum;

    @Id
    @Basic(optional = false)
    @Column(name = "xrow")
    private Integer xrow;

    @Column(name = "xdocrow")
    private Integer xdocrow;

    @Column(name = "xitem")
    private Integer xitem;

    @Column(name = "xqty", precision = 15, scale = 2)
    private BigDecimal xqty;

    @Column(name = "xqtyord")
    private Integer xqtyord;

    @Column(name = "xrate", precision = 15, scale = 2)
    private BigDecimal xrate;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xqtycrn", precision = 15, scale = 2)
    private BigDecimal xqtycrn;

    @Column(name = "xnote", length = 200)
    private String xnote;

    public static Pogrndetail getDefaultInstance() {
        Pogrndetail obj = new Pogrndetail();
        // Set default values here if needed
        return obj;
    }
}
