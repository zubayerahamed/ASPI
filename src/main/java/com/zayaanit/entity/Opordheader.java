package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.OpordheaderPK;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Order Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "opordheader")
@IdClass(OpordheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opordheader extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536083L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xordernum")
    private Integer xordernum;

    @Column(name = "xdate")
    private Date xdate;

    @Column(name = "xbuid")
    private Integer xbuid;

    @Column(name = "xcus")
    private Integer xcus;

    @Column(name = "xwh")
    private Integer xwh;

    @Column(name = "xref", length = 100)
    private String xref;

    @Column(name = "xlineamt", precision = 15, scale = 2)
    private BigDecimal xlineamt;

    @Column(name = "xdiscamt", precision = 15, scale = 2)
    private BigDecimal xdiscamt;

    @Column(name = "xtotamt", precision = 15, scale = 2)
    private BigDecimal xtotamt;

    @Column(name = "xstatus", length = 25)
    private String xstatus;

    @Column(name = "xstatusord", length = 50)
    private String xstatusord;

    @Column(name = "xdornum")
    private Integer xdornum;

    @Column(name = "xstaff")
    private Integer xstaff;

    @Column(name = "xnote", length = 200)
    private String xnote;

    @Column(name = "xstaffsubmit")
    private Integer xstaffsubmit;

    @Column(name = "xsubmittime")
    private Date xsubmittime;

    @Column(name = "xstaffappr")
    private Integer xstaffappr;

    @Column(name = "xapprovertime")
    private Date xapprovertime;

    public static Opordheader getDefaultInstance() {
        Opordheader obj = new Opordheader();
        // Set default values here if needed
        return obj;
    }
}
