package com.zayaanit.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.entity.pk.TempvoucherPK;
import com.zayaanit.enums.SubmitFor;

import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@Entity
@Table(name = "tempvoucher")
@IdClass(TempvoucherPK.class)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "FA_ImportVoucher", procedureName = "FA_ImportVoucher", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "post", type = Boolean.class),
	}),
})
public class Tempvoucher implements Serializable {

	private static final long serialVersionUID = 1685761788841395819L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "Voucher_Date")
	private Date voucherDate;

	@Column(name = "Business_Unit")
	private Integer businessUnit;

	@Column(name = "Debit_Acc")
	private Integer debitAcc;

	@Column(name = "Debit_SubAcc")
	private Integer debitSubAcc;

	@Column(name = "Credit_Acc")
	private Integer creditAcc;

	@Column(name = "Credit_SubAcc")
	private Integer creditSubAcc;

	@Column(name = "Amount", precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "Narration", length = 200)
	private String narration;

	@Column(name = "AllOk")
	private Boolean allOk;

	@Column(name = "ErrorDetails")
	private String errorDetails;

	@Transient
	private String businessUnitName;
	@Transient
	private String debitAccountName;
	@Transient
	private String debitSubAccountName;
	@Transient
	private String creditAccountName;
	@Transient
	private String creditSubAccountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Tempvoucher getDefaultInstance() {
		Tempvoucher obj = new Tempvoucher();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setVoucherDate(new Date());
		obj.setAmount(BigDecimal.ZERO);
		return obj;
	}
}
