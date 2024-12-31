package com.zayaanit.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zayaanit.entity.pk.ImtogliPK;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for Imtogli.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imtogli")
@IdClass(ImtogliPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtogli extends AbstractModel<String> {

    private static final long serialVersionUID = 1681419879800536145L;

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
    @Column(name = "xgitem", length = 25)
    private String xgitem;

    @Column(name = "xaccdr")
    private Integer xaccdr;

    @Column(name = "xacccr")
    private Integer xacccr;

    public static Imtogli getDefaultInstance() {
        Imtogli obj = new Imtogli();
        // Set default values if needed
        return obj;
    }
}
