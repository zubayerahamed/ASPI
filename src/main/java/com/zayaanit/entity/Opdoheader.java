package com.zayaanit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.KitSessionManager;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@Entity
@Table(name = "opdoheader")
@IdClass(OpdoheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "SO_ConfirmInvoice", procedureName = "SO_ConfirmInvoice", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "dornum", type = Integer.class), 
	}),
})
public class Opdoheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536090L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private Integer xdornum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
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

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Column(name = "xordernum")
	private Integer xordernum;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Column(name = "xtotcost", precision = 15, scale = 2)
	private BigDecimal xtotcost;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Transient
	private String businessUnitName;
	@Transient
	private String customerName;
	@Transient
	private String warehouseName;
	@Transient
	private String staffName;
	@Transient
	private String submitStaffName;
	@Transient
	private boolean xislock;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Opdoheader getDefaultInstance() {
		Opdoheader obj = new Opdoheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscamt(BigDecimal.ZERO);
		obj.setXtotamt(obj.getXlineamt().subtract(obj.getXdiscamt()));
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXstatusjv("Open");
		obj.setXtotcost(BigDecimal.ZERO);
		obj.setXtype("Direct Invoice");
		return obj;
	}

	public static Opdoheader getDefaultInstance(List<Cabunit> cabunits) {
		Opdoheader obj = getDefaultInstance();
		if(cabunits != null && cabunits.size() == 1) {
			obj.setXbuid(cabunits.get(0).getXbuid());
			obj.setBusinessUnitName(cabunits.get(0).getXname());
		}
		return obj;
	}

	public static Opdoheader getPOSInstance(KitSessionManager sessionManager) {
		Opdoheader obj = new Opdoheader();
		obj.setSubmitFor(SubmitFor.INSERT);

		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXdiscamt(BigDecimal.ZERO);
		obj.setXtotamt(obj.getXlineamt().subtract(obj.getXdiscamt()));
		obj.setXstatus("Confirmed");
		obj.setXstatusim("Open");
		obj.setXstatusjv("Open");
		obj.setXtotcost(BigDecimal.ZERO);
		obj.setXtype("POS Invoice");

		obj.setXdate(new Date());
		obj.setXstaff(sessionManager.getLoggedInUserDetails().getXstaff());
		obj.setXbuid(sessionManager.getLoggedInUserDetails().getPosunit());
		obj.setXwh(sessionManager.getLoggedInUserDetails().getPosoutlet());
		obj.setXislock(sessionManager.getLoggedInUserDetails().isXislock());
		return obj;
	}

	public static Opdoheader getPOSInstance(KitSessionManager sessionManager, List<Cabunit> cabunits) {
		Opdoheader obj = getPOSInstance(sessionManager);
		if(obj.getXbuid() == null && cabunits != null && cabunits.size() == 1) {
			obj.setXbuid(cabunits.get(0).getXbuid());
			obj.setBusinessUnitName(cabunits.get(0).getXname());
		}
		return obj;
	}

	public Opdoheader build(KitSessionManager sessionManager, AcsubRepo acsubRepo, CabunitRepo cabunitRepo,  XwhsRepo xwhsRepo) {
		if(this.getXbuid() != null) {
			Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), this.getXbuid()));
			if(cabunitOp.isPresent()) this.setBusinessUnitName(cabunitOp.get().getXname());
		}

		if(this.getXwh() != null) {
			Optional<Xwhs> xwhsOp = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), this.getXwh()));
			if(xwhsOp.isPresent()) this.setWarehouseName(xwhsOp.get().getXname());
		}

		if(this.getXstaff() != null) {
			Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), this.getXstaff()));
			if(acsubOp.isPresent()) this.setStaffName(acsubOp.get().getXname());
		}

		this.xislock = sessionManager.getLoggedInUserDetails().isXislock();

		return this;
	}
}
