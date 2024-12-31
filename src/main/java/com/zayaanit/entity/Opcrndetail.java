package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.OpcrndetailPK;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for CRN Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "opcrndetail")
@IdClass(OpcrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opcrndetail extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536105L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xcrnnum")
    private Integer xcrnnum;

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

    @Column(name = "xqtyinv", precision = 15, scale = 2)
    private BigDecimal xqtyinv;

    @Column(name = "xrate", precision = 15, scale = 2)
    private BigDecimal xrate;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xrategrn", precision = 15, scale = 2)
    private BigDecimal xrategrn;

    @Column(name = "xnote", length = 200)
    private String xnote;

    public static Opcrndetail getDefaultInstance() {
        Opcrndetail obj = new Opcrndetail();
        // Set default values if needed
        return obj;
    }
}
