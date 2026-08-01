package com.zayaanit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.service.XmenusService;
import com.zayaanit.service.XprofilesService;
import com.zayaanit.service.XscreensService;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController extends KitController {

	@Autowired private XmenusService xmenusService;
	@Autowired private XscreensService xscreensService;
	@Autowired private XprofilesService profileService;

	@PostMapping("/table/{fragmentcode}/{suffix}")
	public String loadHeaderTableFragment(
			@RequestParam(required = false) String hint, 
			@RequestParam(required = false) String dependentparam,
			@RequestParam(required = false) String resetparam, 
			@PathVariable String fragmentcode, 
			@PathVariable int suffix, 
			String fieldId, 
			boolean mainscreen,
			String mainreloadurl,
			String mainreloadid,
			String extrafieldcontroller,
			String detailreloadurl,
			String detailreloadid,
			String additionalreloadurl,
			String additionalreloadid,
			Model model){

		model.addAttribute("suffix", suffix);
		model.addAttribute("searchValue", hint);
		model.addAttribute("dependentParam", dependentparam);
		model.addAttribute("resetParam", resetparam);
		model.addAttribute("fieldId", fieldId);
		model.addAttribute("mainscreen", mainscreen);
		model.addAttribute("mainreloadurl", mainreloadurl);
		model.addAttribute("mainreloadid", mainreloadid);
		model.addAttribute("extrafieldcontroller", extrafieldcontroller);
		model.addAttribute("detailreloadurl", detailreloadurl);
		model.addAttribute("detailreloadid", detailreloadid);
		model.addAttribute("additionalreloadurl", additionalreloadurl);
		model.addAttribute("additionalreloadid", additionalreloadid);
		return "search-fragments::" + fragmentcode + "-table";
	}

	@GetMapping("/LSA11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xmenus> LSA11(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xmenus> list = xmenusService.LSA11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xmenusService.LSA11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xmenus> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSA12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xscreens> LSA12(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xscreens> list = xscreensService.LSA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xscreensService.LSA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xscreens> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LAD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xprofiles> LAD12(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xprofiles> list = profileService.LAD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = profileService.LAD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xprofiles> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}
	

	@Override
	protected String screenCode() {
		return null;
	}

	@Override
	protected String pageTitle() {
		return null;
	}

	
}
