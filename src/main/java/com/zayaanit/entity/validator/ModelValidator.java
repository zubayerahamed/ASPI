package com.zayaanit.entity.validator;

import java.util.Optional;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.zayaanit.entity.Acdef;
import com.zayaanit.entity.Acgroup;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imissueheader;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Pocrnheader;
import com.zayaanit.entity.Pogrnheader;
import com.zayaanit.entity.Poordheader;
import com.zayaanit.entity.Potogli;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xmenuscreens;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.AcgroupPK;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.PotogliPK;
import com.zayaanit.entity.pk.XcodesPK;
import com.zayaanit.entity.pk.XmenusPK;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.repository.AcgroupRepo;
import com.zayaanit.repository.AcmstRepo;
import com.zayaanit.repository.AcsubRepo;
import com.zayaanit.repository.CabunitRepo;
import com.zayaanit.repository.PotogliRepo;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XmenusRepo;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XscreensRepo;
import com.zayaanit.repository.XusersRepo;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.KitSessionManager;
/**
 * @author Zubayer Ahamed
 * @since Dec 04, 2020
 */
@Component
public class ModelValidator extends ConstraintValidator {

	@Autowired private XmenusRepo xmenusRepo;
	@Autowired private XscreensRepo xscreensRepo;
	@Autowired private KitSessionManager sessionManager;
	@Autowired private XprofilesRepo profileRepo;
	@Autowired private XcodesRepo xcodesRepo;
	@Autowired private XusersRepo xusersRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private AcgroupRepo acgroupRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private PotogliRepo potogliRepo;

	public void validateXmenus(Xmenus xmenus, Errors errors, Validator validator) {
		if(xmenus == null) return;

		super.validate(xmenus, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Xmenus> op = xmenusRepo.findById(new XmenusPK(sessionManager.getBusinessId(), xmenus.getXmenu()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xmenus.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xmenu", "Menu already exist in the system");
		}
	}

	public void validateXcodes(Xcodes xcodes, Errors errors, Validator validator) {
		if(xcodes == null) return;

		super.validate(xcodes, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xcodes.getXtype(), xcodes.getXcode()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xcodes.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xcode", "Code already exist in the system");
		}
	}

	public void validatePotogli(Potogli potogli, Errors errors, Validator validator) {
		if(potogli == null) return;

		super.validate(potogli, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Potogli> op = potogliRepo.findById(new PotogliPK(sessionManager.getBusinessId(), potogli.getXtype(), potogli.getXgsup(), potogli.getXgitem()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(potogli.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xtype", "Data already exist in the system");
		}
	}

	public void validateXmenuscreens(Xmenuscreens xmenuscreens, Errors errors, Validator validator) {
		if(xmenuscreens == null) return;

		super.validate(xmenuscreens, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateXscreens(Xscreens xscreens, Errors errors, Validator validator) {
		if(xscreens == null) return;

		super.validate(xscreens, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Xscreens> op = xscreensRepo.findById(new XscreensPK(sessionManager.getBusinessId(), xscreens.getXscreen()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xscreens.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xscreen", "Screen already exist in the system");
		}
	}

	public void validateProfile(Xprofiles profile, Errors errors, Validator validator) {
		if(profile == null) return;

		super.validate(profile, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), profile.getXprofile()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(profile.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xprofile", "Profile already exist in the system");
		}
	}

	

	public void validateXusers(Xusers xusers, Errors errors, Validator validator) {
		if(xusers == null) return;

		super.validate(xusers, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Xusers> op = xusersRepo.findById(new XusersPK(sessionManager.getBusinessId(), xusers.getZemail()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xusers.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("zemail", "User already exist in the system");
		}
	}

	public void validateCabunit(Cabunit cabunit, Errors errors, Validator validator) {
		if(cabunit == null) return;

		super.validate(cabunit, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Cabunit> op = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), cabunit.getXbuid()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(cabunit.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xbuid", "Business unit exist in the system");
		}
	}

	public void validateXwhs(Xwhs xwhs, Errors errors, Validator validator) {
		if(xwhs == null) return;

		super.validate(xwhs, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwhs.getXwh()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xwhs.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xwh", "Store code exist in the system");
		}
	}

	public void validateAcgroup(Acgroup acgroup, Errors errors, Validator validator) {
		if(acgroup == null) return;

		super.validate(acgroup, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Acgroup> op = acgroupRepo.findById(new AcgroupPK(sessionManager.getBusinessId(), acgroup.getXagcode()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(acgroup.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xagcode", "Group code exist in the system");
		}
	}

	public void validateAcmst(Acmst acmst, Errors errors, Validator validator) {
		if(acmst == null) return;

		super.validate(acmst, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Acmst> op = acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acmst.getXacc()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(acmst.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xacc", "Account exist in the system");
		}
	}

	public void validateAcsub(Acsub acsub, Errors errors, Validator validator) {
		if(acsub == null) return;

		super.validate(acsub, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Acsub> op = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acsub.getXsub()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(acsub.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xsub", "Account exist in the system");
		}
	}

	public void validateAcheader(Acheader acheader, Errors errors, Validator validator) {
		if(acheader == null) return;

		super.validate(acheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validatePoordheader(Poordheader poordheader, Errors errors, Validator validator) {
		if(poordheader == null) return;

		super.validate(poordheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateOpordheader(Opordheader opordheader, Errors errors, Validator validator) {
		if(opordheader == null) return;

		super.validate(opordheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateOpdoheader(Opdoheader opdoheader, Errors errors, Validator validator) {
		if(opdoheader == null) return;

		super.validate(opdoheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validatePogrnheader(Pogrnheader pogrnheader, Errors errors, Validator validator) {
		if(pogrnheader == null) return;

		super.validate(pogrnheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateImtorheader(Imtorheader imtorheader, Errors errors, Validator validator) {
		if(imtorheader == null) return;

		super.validate(imtorheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateImissueheader(Imissueheader imissueheader, Errors errors, Validator validator) {
		if(imissueheader == null) return;

		super.validate(imissueheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validatePocrnheader(Pocrnheader pocrnheader, Errors errors, Validator validator) {
		if(pocrnheader == null) return;

		super.validate(pocrnheader, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateCaitem(Caitem caitem, Errors errors, Validator validator) {
		if(caitem == null) return;

		super.validate(caitem, errors, validator);
		if (errors.hasErrors()) return;
	}

	public void validateZbusiness(Zbusiness zbusiness, Errors errors, Validator validator) {
		if(zbusiness == null) return;
		super.validate(zbusiness, errors, validator);
	}

	public void validateAcdef(Acdef acdef, Errors errors, Validator validator) {
		if(acdef == null) return;
		super.validate(acdef, errors, validator);
	}

}
