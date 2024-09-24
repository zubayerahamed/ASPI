package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xprofilesdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XprofilesPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.repository.XprofilesRepo;
import com.zayaanit.repository.XprofilesdtRepo;
import com.zayaanit.service.XprofilesService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 5, 2023
 */
@Slf4j
@Controller
@RequestMapping("/AD12")
public class AD12 extends KitController {

	@Autowired private XprofilesService profileService;
	@Autowired private XprofilesRepo profileRepo;
	@Autowired private XprofilesdtRepo profileDtRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "AD12";
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD12"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(Model model) {
		model.addAttribute("profile", Xprofiles.getDefaultInstance());
		return "pages/AD12/AD12";
	}

	@GetMapping("/{xprofile}")
	public String loadFormFragment(@PathVariable String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("Xprofiles", Xprofiles.getDefaultInstance());
			return "pages/AD12/AD12-fragments::main-form";
		}

		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		model.addAttribute("Xprofiles", op.isPresent() ? op.get() : Xprofiles.getDefaultInstance());
		return "pages/AD12/AD12-fragments::main-form";
	}

	@PostMapping("/index")
	public String index2(String xprofile, Model model) {
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		model.addAttribute("Xprofiles", op.isPresent() ? op.get() : Xprofiles.getDefaultInstance());
		return "pages/AD12/AD12-fragments::main-form";
	}

	@GetMapping("/detail-table")
	public String loadDetailTableFragment(@RequestParam String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("screens", Collections.emptyList());
			return "pages/AD12/AD12-fragments::detail-table";
		}

		Map<String, ScreenOptions> map = new HashMap<>();

		List<Xscreens> screens = xscreenRepo.findAllByXtypeAndZid("Screen", sessionManager.getBusinessId());
		for(Xscreens screen : screens) {
			ScreenOptions op = new ScreenOptions();
			op.setXscreen(screen.getXscreen());
			op.setXtitle(screen.getXtitle());
			map.put(screen.getXscreen(), op);
		}

		List<Xprofilesdt> profileDtList = profileDtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		for(Xprofilesdt pdt : profileDtList) {
			if(map.get(pdt.getXscreen()) != null) {
				map.get(pdt.getXscreen()).setSelected(true);
			}
		}

		List<ScreenOptions> scOptions = new ArrayList<>();
		for(Map.Entry<String, ScreenOptions> m : map.entrySet()) {
			scOptions.add(m.getValue());
		}

		scOptions.sort(Comparator.comparing(ScreenOptions::getSequence));

		boolean allSelected = false;
		if(scOptions.size() == scOptions.stream().filter(f -> f.isSelected()).count()) {
			allSelected = true;
		}
		model.addAttribute("allSelected", allSelected);
		model.addAttribute("screens", scOptions);
		model.addAttribute("profileName", xprofile);
		return "pages/AD12/AD12-fragments::detail-table";
	}

	@PostMapping("/detail-table")
	public String loadDetailTableFragment2(String xprofile, Model model) {
		if("RESET".equalsIgnoreCase(xprofile)) {
			model.addAttribute("screens", Collections.emptyList());
			return "pages/AD12/AD12-fragments::detail-table";
		}

		Map<String, ScreenOptions> map = new HashMap<>();

		List<Xscreens> screens = xscreenRepo.findAllByXtypeAndZid("Screen", sessionManager.getBusinessId());
		for(Xscreens screen : screens) {
			ScreenOptions op = new ScreenOptions();
			op.setXscreen(screen.getXscreen());
			op.setXtitle(screen.getXtitle());
			map.put(screen.getXscreen(), op);
		}

		List<Xprofilesdt> profileDtList = profileDtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		for(Xprofilesdt pdt : profileDtList) {
			if(map.get(pdt.getXscreen()) != null) {
				map.get(pdt.getXscreen()).setSelected(true);
			}
		}

		List<ScreenOptions> scOptions = new ArrayList<>();
		for(Map.Entry<String, ScreenOptions> m : map.entrySet()) {
			scOptions.add(m.getValue());
		}

		scOptions.sort(Comparator.comparing(ScreenOptions::getSequence));

		boolean allSelected = false;
		if(scOptions.size() == scOptions.stream().filter(f -> f.isSelected()).count()) {
			allSelected = true;
		}
		model.addAttribute("allSelected", allSelected);
		model.addAttribute("screens", scOptions);
		model.addAttribute("profileName", xprofile);
		return "pages/AD12/AD12-fragments::detail-table";
	}

	@GetMapping("/header-table")
	public String loadHeaderTableFragment(Model model) {
		return "pages/AD12/AD12-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Xprofiles> getAll(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xprofiles> profiles = profileService.LAD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0);
		int totalRows = profileService.LAD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0);

		DatatableResponseHelper<Xprofiles> response = new DatatableResponseHelper<Xprofiles>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(profiles);
		return response;
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(Xprofiles Xprofiles, BindingResult bindingResult){

		// VALIDATE XSCREENS
		modelValidator.validateProfile(Xprofiles, bindingResult, validator);
		if(bindingResult.hasErrors()) return modelValidator.getValidationMessage(bindingResult);

		// Create new
		if(SubmitFor.INSERT.equals(Xprofiles.getSubmitFor())) {
			Xprofiles.setZid(sessionManager.getBusinessId());
			Xprofiles = profileRepo.save(Xprofiles);

			List<ReloadSectionParams> postData = new ArrayList<>();
			postData.add(new ReloadSectionParams("xprofile", Xprofiles.getXprofile()));

			List<ReloadSection> reloadSections = new ArrayList<>();
			reloadSections.add(new ReloadSection("main-form-container", "/AD12/index", postData));
			reloadSections.add(new ReloadSection("header-table-container", "/AD12/header-table"));
			reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table", postData));
			responseHelper.setReloadSections(reloadSections);
			responseHelper.setSuccessStatusAndMessage("Saved successfully");
			return responseHelper.getResponse();
		}

		// Update existing
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), Xprofiles.getXprofile()));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do update");
			return responseHelper.getResponse();
		}

		Xprofiles existObj = op.get();
		BeanUtils.copyProperties(Xprofiles, existObj, "zid", "zuserid", "ztime", "xprofile");
		existObj = profileRepo.save(existObj);

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xprofile", Xprofiles.getXprofile()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD12/index", postData));
		reloadSections.add(new ReloadSection("header-table-container", "/AD12/header-table"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Updated successfully");
		return responseHelper.getResponse();
	}

	@PostMapping(value = "/detail/save", headers="Accept=application/json")
	public @ResponseBody Map<String, Object> saveDetail(@RequestBody String json){
		DetailData dd = new DetailData();

		ObjectMapper obm = new ObjectMapper();
		try {
			dd = obm.readValue(json, DetailData.class);
		} catch (JsonProcessingException e) {
			log.error(ERROR, e.getMessage(), e); 
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		List<Xprofilesdt> existingData = profileDtRepo.findAllByXprofileAndZid(dd.getProfileName(), sessionManager.getBusinessId());
		if(existingData != null && !existingData.isEmpty()) {
			profileDtRepo.deleteAll(existingData);
		}

		List<Xprofilesdt> list = new ArrayList<>();
		for(String xscreen : dd.getXscreens()) {
			Xprofilesdt dt = new Xprofilesdt();
			dt.setXprofile(dd.getProfileName());
			dt.setZid(sessionManager.getBusinessId());
			dt.setXscreen(xscreen);
			list.add(dt);
		}

		List<Xprofilesdt> savedlist = profileDtRepo.saveAll(list);

		if(savedlist == null) {
			responseHelper.setErrorStatusAndMessage("Can't save");
			return responseHelper.getResponse();
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xprofile", dd.getProfileName()));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Details saved successfully");
		return responseHelper.getResponse();
	}

	@DeleteMapping
	public @ResponseBody Map<String, Object> delete(String xprofile){
		Optional<Xprofiles> op = profileRepo.findById(new XprofilesPK(sessionManager.getBusinessId(), xprofile));
		if(!op.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Data not found in this system to do delete");
			return responseHelper.getResponse();
		}

		List<Xprofilesdt> details = profiledtRepo.findAllByXprofileAndZid(xprofile, sessionManager.getBusinessId());
		if(details != null && !details.isEmpty()) {
			profiledtRepo.deleteAll(details);
		}

		Xprofiles obj = op.get();
		profileRepo.delete(obj);

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xprofile", "RESET"));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/AD12/index", postData));
		reloadSections.add(new ReloadSection("header-table-container", "/AD12/header-table"));
		reloadSections.add(new ReloadSection("detail-table-container", "/AD12/detail-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Deleted successfully");
		return responseHelper.getResponse();
	}
}

@Data
class ScreenOptions{
	private String xscreen;
	private String xtitle;
	private boolean selected;
	private Integer sequence;
}

@Data
class DetailData{
	private String[] xscreens;
	private String profileName;
}
