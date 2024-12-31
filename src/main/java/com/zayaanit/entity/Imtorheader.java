package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ImtorheaderPK;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for IMTOR Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imtorheader")
@IdClass(ImtorheaderPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtorheader extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536107L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xtornum")
    private Integer xtornum;

    @Column(name = "xdate")
    private Date xdate;

    @Column(name = "xfbuid")
    private Integer xfbuid;

    @Column(name = "xtbuid")
    private Integer xtbuid;

    @Column(name = "xfwh")
    private Integer xfwh;

    @Column(name = "xtwh")
    private Integer xtwh;

    @Column(name = "xref", length = 100)
    private String xref;

    @Column(name = "xstatus", length = 25)
    private String xstatus;

    @Column(name = "xstatusim", length = 25)
    private String xstatusim;

    @Column(name = "xtotamt", precision = 15, scale = 2)
    private BigDecimal xtotamt;

    @Column(name = "xstaff")
    private Integer xstaff;

    @Column(name = "xnote", length = 200)
    private String xnote;

    @Column(name = "xsubmittime")
    private Date xsubmittime;

    @Column(name = "xstaffsubmit")
    private Integer xstaffsubmit;

    public static Imtorheader getDefaultInstance() {
        Imtorheader obj = new Imtorheader();
        // Set default values if needed
        return obj;
    }
}
