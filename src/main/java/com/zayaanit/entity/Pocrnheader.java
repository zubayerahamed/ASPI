package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.PocrnheaderPK;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for CRN Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "pocrnheader")
@IdClass(PocrnheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Pocrnheader extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536079L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xcrnnum")
    private Integer xcrnnum;

    @Column(name = "xdate")
    private Date xdate;

    @Column(name = "xgrnnum")
    private Integer xgrnnum;

    @Column(name = "xcus")
    private Integer xcus;

    @Column(name = "xbuid")
    private Integer xbuid;

    @Column(name = "xwh")
    private Integer xwh;

    @Column(name = "xtotamt", precision = 15, scale = 2)
    private BigDecimal xtotamt;

    @Column(name = "xtotcost", precision = 15, scale = 2)
    private BigDecimal xtotcost;

    @Column(name = "xref", length = 100)
    private String xref;

    @Column(name = "xstaff")
    private Integer xstaff;

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

    @Column(name = "xtype", length = 25)
    private String xtype;

    @Column(name = "xstaffsubmit")
    private Integer xstaffsubmit;

    @Column(name = "xsubmittime")
    private Date xsubmittime;

    public static Pocrnheader getDefaultInstance() {
        Pocrnheader obj = new Pocrnheader();
        // Set default values here if needed
        return obj;
    }
}
