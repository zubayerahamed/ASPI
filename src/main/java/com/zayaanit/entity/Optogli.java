package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.OptogliPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Optogli.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "optogli")
@IdClass(OptogliPK.class)
@EqualsAndHashCode(callSuper = true)
public class Optogli extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536143L;

    @Id
    @Basic(optional = false)
    @Column(name = "zid")
    private Integer zid;

    @Id
    @Basic(optional = false)
    @Column(name = "xtype", length = 25)
    private String xtype;

    @Id
    @Basic(optional = false)
    @Column(name = "xgcus", length = 25)
    private String xgcus;

    @Id
    @Basic(optional = false)
    @Column(name = "xgitem", length = 25)
    private String xgitem;

    @Column(name = "xacccr")
    private Integer xacccr;

    @Column(name = "xaccdisc")
    private Integer xaccdisc;

    public static Optogli getDefaultInstance() {
        Optogli obj = new Optogli();
        // Set default values if needed
        return obj;
    }
}
