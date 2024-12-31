package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.MoheaderPK;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for MO Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "moheader")
@IdClass(MoheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Moheader extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536137L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xbatch")
    private Integer xbatch;

    @Column(name = "xdate")
    private Date xdate;

    @Column(name = "xbuid")
    private Integer xbuid;

    @Column(name = "xwh")
    private Integer xwh;

    @Column(name = "xitem")
    private Integer xitem;

    @Column(name = "xqty", precision = 15, scale = 2)
    private BigDecimal xqty;

    @Column(name = "xrate", precision = 15, scale = 2)
    private BigDecimal xrate;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xstatus", length = 25)
    private String xstatus;

    @Column(name = "xstatusim", length = 25)
    private String xstatusim;

    @Column(name = "xstatusjv", length = 25)
    private String xstatusjv;

    @Column(name = "xvoucher")
    private Integer xvoucher;

    @Column(name = "xnote", length = 200)
    private String xnote;

    @Column(name = "xsubmittime")
    private Date xsubmittime;

    @Column(name = "xstaffsubmit")
    private Integer xstaffsubmit;

    public static Moheader getDefaultInstance() {
        Moheader obj = new Moheader();
        // Set default values if needed
        return obj;
    }
}
