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

import com.zayaanit.entity.Acgroup;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Xmenus;
import com.zayaanit.entity.Xprofiles;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Xusers;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.service.AcgroupService;
import com.zayaanit.service.AcheaderService;
import com.zayaanit.service.AcmstService;
import com.zayaanit.service.AcsubService;
import com.zayaanit.service.CabunitService;
import com.zayaanit.service.XmenusService;
import com.zayaanit.service.XprofilesService;
import com.zayaanit.service.XscreensService;
import com.zayaanit.service.XusersService;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController {

	@Autowired private XmenusService xmenusService;
	@Autowired private XscreensService xscreensService;
	@Autowired private XprofilesService profileService;
	@Autowired private XusersService xusersService;
	@Autowired private CabunitService cabunitService;
	@Autowired private AcgroupService acgroupService;
	@Autowired private AcmstService acmstService;
	@Autowired private AcsubService acsubService;
	@Autowired private AcheaderService acheaderService;

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
	public @ResponseBody DatatableResponseHelper<Xmenus> LSA11(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xmenus> list = xmenusService.LSA11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xmenusService.LSA11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xmenus> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSA12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xscreens> LSA12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xscreens> list = xscreensService.LSA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xscreensService.LSA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xscreens> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LAD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xprofiles> LAD12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xprofiles> list = profileService.LAD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = profileService.LAD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xprofiles> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LAD13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xusers> LAD13(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xusers> list = xusersService.LAD13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xusersService.LAD13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xusers> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LAD17/{suffix}")
	public @ResponseBody DatatableResponseHelper<Cabunit> LAD17(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Cabunit> list = cabunitService.LAD17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = cabunitService.LAD17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Cabunit> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acgroup> LFA12(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acgroup> list = acgroupService.LFA12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acgroupService.LFA12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acgroup> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acmst> LFA13(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acmst> list = acmstService.LFA13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acmstService.LFA13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acmst> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acsub> LFA14(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acsub> list = acsubService.LFA14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acsubService.LFA14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acsub> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA15/{suffix}")
	public @ResponseBody DatatableResponseHelper<Acheader> LFA15(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Acheader> list = acheaderService.LFA15(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = acheaderService.LFA15(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Acheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}
}
