package com.zayaanit.entity.validator;

import java.util.Optional;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Casms;
import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Opvhls;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Profile;
import com.zayaanit.entity.Termsdef;
import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.ArhedPK;
import com.zayaanit.entity.pk.OpareaPK;
import com.zayaanit.entity.pk.PdmstPK;
import com.zayaanit.entity.pk.ProfilePK;
import com.zayaanit.entity.pk.TermsdefPK;
import com.zayaanit.entity.pk.XcodesPK;
import com.zayaanit.entity.pk.XorgsPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.entity.pk.XusersPK;
import com.zayaanit.entity.pk.XwhsPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.repository.ArhedRepo;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.ProfileRepo;
import com.zayaanit.repository.TermsdefRepo;
import com.zayaanit.repository.XcodesRepo;
import com.zayaanit.repository.XorgsRepo;
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

	@Autowired private XscreensRepo xscreensRepo;
	@Autowired private KitSessionManager sessionManager;
	@Autowired private ProfileRepo profileRepo;
	@Autowired private XcodesRepo xcodesRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private OpareaRepo opareaRepo;
	@Autowired private XusersRepo xusersRepo;
	@Autowired private ArhedRepo arhedRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private XorgsRepo xorgsRepo;
	@Autowired private TermsdefRepo termsdefRepo;

	public void validateXcodes(Xcodes xcodes, Errors errors, Validator validator) {
		if(xcodes == null) return;

		super.validate(xcodes, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Xcodes> op = xcodesRepo.findById(new XcodesPK(sessionManager.getBusinessId(), xcodes.getXtype(), xcodes.getXcode()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xcodes.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xcodes", "Code already exist in the system");
		}
	}

	public void validateArhed(Arhed arhed, Errors errors, Validator validator) {
		if(arhed == null) return;

		super.validate(arhed, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Arhed> op = arhedRepo.findById(new ArhedPK(sessionManager.getBusinessId(), arhed.getXtrnnum(), arhed.getXscreen()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(arhed.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xtrnnum", "Receipt Number already exist in the system");
		}
	}

	public void validateXwhs(Xwhs xwhs, Errors errors, Validator validator) {
		if(xwhs == null) return;

		super.validate(xwhs, errors, validator);
		if (errors.hasErrors()) return;

		// check xscreen already exist
		Optional<Xwhs> op = xwhsRepo.findById(new XwhsPK(sessionManager.getBusinessId(), xwhs.getXwh()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xwhs.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xwh", "Store code already exist in the system");
		}
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

	public void validateProfile(Profile profile, Errors errors, Validator validator) {
		if(profile == null) return;

		super.validate(profile, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Profile> op = profileRepo.findById(new ProfilePK(sessionManager.getBusinessId(), profile.getXprofile()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(profile.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xprofile", "Profile already exist in the system");
		}
	}

	public void validateOparea(Oparea oparea, Errors errors, Validator validator) {
		if(oparea == null) return;

		super.validate(oparea, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Oparea> op = opareaRepo.findById(new OpareaPK(sessionManager.getBusinessId(), oparea.getXarea()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(oparea.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xarea", "Area already exist in the system");
		}
	}

	public void validateXorgs(Xorgs xorgs, Errors errors, Validator validator) {
		if(xorgs == null) return;

		super.validate(xorgs, errors, validator);
		if (errors.hasErrors()) return;

		// check profile already exist
		Optional<Xorgs> op = xorgsRepo.findById(new XorgsPK(sessionManager.getBusinessId(), xorgs.getXorg()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(xorgs.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xorg", "Org already exist in the system");
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

	public void validateZbusiness(Zbusiness zbusiness, Errors errors, Validator validator) {
		if(zbusiness == null) return;
		super.validate(zbusiness, errors, validator);
	}

	public void validatePdmst(Pdmst pdmst, Errors errors, Validator validator) {
		if(pdmst == null) return;
		super.validate(pdmst, errors, validator);
		
		// check profile already exist
		Optional<Pdmst> op = pdmstRepo.findById(new PdmstPK(sessionManager.getBusinessId(), pdmst.getXstaff()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(pdmst.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xstaff", "This employee ID is already exists.");
		}
	}

	public void validateCaitem(Caitem caitem, Errors errors, Validator validator) {
		if(caitem == null) return;
		super.validate(caitem, errors, validator);
	}

	public void validateCasms(Casms casms, Errors errors, Validator validator) {
		if(casms == null) return;
		super.validate(casms, errors, validator);
	}

	public void validateCacus(Cacus cacus, Errors errors, Validator validator) {
		if(cacus == null) return;
		super.validate(cacus, errors, validator);
	}

	public void validateOpvhls(Opvhls opvhls, Errors errors, Validator validator) {
		if(opvhls == null) return;
		super.validate(opvhls, errors, validator);
	}

	public void validateCabank(Cabank cabank, Errors errors, Validator validator) {
		if(cabank == null) return;
		super.validate(cabank, errors, validator);
	}

	public void validateTermsdef(Termsdef termsdef, Errors errors, Validator validator) {
		if(termsdef == null) return;
		super.validate(termsdef, errors, validator);

		// check termsdef already exist
		Optional<Termsdef> op = termsdefRepo.findById(new TermsdefPK(sessionManager.getBusinessId(), termsdef.getXtype(), termsdef.getXterm()));
		if(!op.isPresent()) return;

		if(SubmitFor.INSERT.equals(termsdef.getSubmitFor()) && op.isPresent()) {
			errors.rejectValue("xterm", "Terms already exist in the system");
		}
	}
}
