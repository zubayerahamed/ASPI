package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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

import com.zayaanit.entity.Arhed;
import com.zayaanit.entity.Cabank;
import com.zayaanit.entity.Cacus;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Imadjheader;
import com.zayaanit.entity.Imissueheader;
import com.zayaanit.entity.Imrcvheader;
import com.zayaanit.entity.Imtorheader;
import com.zayaanit.entity.Oparea;
import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Opordheader;
import com.zayaanit.entity.Opquotheader;
import com.zayaanit.entity.Opreqheader;
import com.zayaanit.entity.Opships;
import com.zayaanit.entity.Opvhls;
import com.zayaanit.entity.Pdmst;
import com.zayaanit.entity.Profile;
import com.zayaanit.entity.Xorgs;
import com.zayaanit.entity.Xusers;
import com.zayaanit.entity.Xwhs;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.SearchSuggestResult;
import com.zayaanit.repository.CabankRepo;
import com.zayaanit.repository.CacusRepo;
import com.zayaanit.repository.CaitemRepo;
import com.zayaanit.repository.OpareaRepo;
import com.zayaanit.repository.PdmstRepo;
import com.zayaanit.repository.XwhsRepo;
import com.zayaanit.service.AreaCustomerViewService;
import com.zayaanit.service.ArhedService;
import com.zayaanit.service.CabankService;
import com.zayaanit.service.CacusService;
import com.zayaanit.service.CaitemService;
import com.zayaanit.service.ImadjHeaderService;
import com.zayaanit.service.ImissueHeaderService;
import com.zayaanit.service.ImrcvHeaderService;
import com.zayaanit.service.ImtorHeaderService;
import com.zayaanit.service.OpareaService;
import com.zayaanit.service.OpcrnHeaderService;
import com.zayaanit.service.OpdoHeaderService;
import com.zayaanit.service.OpordHeaderService;
import com.zayaanit.service.OpquotheaderService;
import com.zayaanit.service.OpreqHeaderService;
import com.zayaanit.service.OpshipsService;
import com.zayaanit.service.OpvhlsService;
import com.zayaanit.service.PdmstService;
import com.zayaanit.service.ProfileService;
import com.zayaanit.service.XorgsService;
import com.zayaanit.service.XusersService;
import com.zayaanit.service.XwhsService;

/**
 * @author Zubayer Ahamed
 * @since Jul 13, 2023
 */
@Controller
@RequestMapping("/search")
public class SearchSuggestController extends KitController {

	@Autowired private CacusRepo cacusRepo;
	@Autowired private XwhsRepo xwhsRepo;
	@Autowired private PdmstRepo pdmstRepo;
	@Autowired private OpareaRepo opareaRepo;
	@Autowired private CabankRepo cabankRepo;
	@Autowired private CaitemRepo caitemRepo;
	@Autowired private CaitemService caitemService;
	@Autowired private CabankService cabankService;
	@Autowired private OpareaService opareaService;
	@Autowired private PdmstService pdmstService;
	@Autowired private XwhsService xwhsService;
	@Autowired private AreaCustomerViewService acViewService;
	@Autowired private CacusService cacusService;
	@Autowired private ProfileService profileService;
	@Autowired private XusersService xusersService;
	@Autowired private ArhedService arhedService;
	@Autowired private ImtorHeaderService imtorheaderService;
	@Autowired private ImrcvHeaderService imrcvheaderService;
	@Autowired private ImissueHeaderService imissueheaderService;
	@Autowired private ImadjHeaderService imadjheaderService;
	@Autowired private OpreqHeaderService opreqheaderService;
	@Autowired private OpordHeaderService opordheaderService;
	@Autowired private OpdoHeaderService opdoheaderService;
	@Autowired private OpcrnHeaderService opcrnheaderService;
	@Autowired private XorgsService xorgsService;
	@Autowired private OpquotheaderService opquotheaderService;
	@Autowired private OpvhlsService opvhlsService;
	@Autowired private OpshipsService opshipsService;

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

	@GetMapping("/LAD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Profile> searchProfile(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Profile> list = profileService.LAD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = profileService.LAD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Profile> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LAD13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xusers> searchUsers(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xusers> list = xusersService.LAD13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xusersService.LAD13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xusers> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xwhs> searchStores(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xwhs> list = xwhsService.LMD11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xwhsService.LMD11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xwhs> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD1101/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xwhs> LMD1101(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xwhs> list = xwhsService.LMD1101(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xwhsService.LMD1101(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xwhs> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD1102/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xwhs> LMD1102(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xwhs> list = xwhsService.LMD1102(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = xwhsService.LMD1102(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Xwhs> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Pdmst> searchStaff(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Pdmst> list = pdmstService.LMD12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = pdmstService.LMD12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Pdmst> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Caitem> searchItem(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Caitem> list = caitemService.LMD13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = caitemService.LMD13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Caitem> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD1301/{suffix}")
	public @ResponseBody DatatableResponseHelper<Caitem> LMD1301(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Caitem> list = caitemService.LMD1301(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = caitemService.LMD1301(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Caitem> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Cacus> searchCustomer(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Cacus> list = new ArrayList<>();
		int totalRows = 0;
		if(suffix == 0 || suffix == 1) {
			list = cacusService.LMD14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
			totalRows = cacusService.LMD14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		} else {
			list = acViewService.LMD14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
			totalRows = acViewService.LMD14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		}

		DatatableResponseHelper<Cacus> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD15/{suffix}")
	public @ResponseBody DatatableResponseHelper<Cabank> searchBank(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Cabank> list = cabankService.LMD15(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = cabankService.LMD15(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Cabank> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD16/{suffix}")
	public @ResponseBody DatatableResponseHelper<Oparea> searchArea(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Oparea> list = opareaService.LMD16(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opareaService.LMD16(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Oparea> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD17/{suffix}")
	public @ResponseBody DatatableResponseHelper<Xorgs> LMD17(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Xorgs> list = xorgsService.LMD17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = xorgsService.LMD17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Xorgs> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LMD18/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opvhls> LMD18(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opvhls> list = opvhlsService.LMD18(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opvhlsService.LMD18(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Opvhls> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA31/{suffix}")
	public @ResponseBody DatatableResponseHelper<Arhed> searchFA31(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.LFA31(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = arhedService.LFA31(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA32/{suffix}")
	public @ResponseBody DatatableResponseHelper<Arhed> searchFA32(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.LFA32(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = arhedService.LFA32(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA33/{suffix}")
	public @ResponseBody DatatableResponseHelper<Arhed> searchFA33(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.LFA33(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = arhedService.LFA33(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA36/{suffix}")
	public @ResponseBody DatatableResponseHelper<Arhed> searchLFA36(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.LFA36(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = arhedService.LFA36(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LFA37/{suffix}")
	public @ResponseBody DatatableResponseHelper<Arhed> searchLFA37(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Arhed> list = arhedService.LFA37(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = arhedService.LFA37(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Arhed> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LIM11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imtorheader> searchLIM11(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imtorheader> list = imtorheaderService.LIM11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = imtorheaderService.LIM11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Imtorheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LIM12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imrcvheader> searchLIM12(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imrcvheader> list = imrcvheaderService.LIM12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = imrcvheaderService.LIM12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Imrcvheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LIM13/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imissueheader> searchLIM13(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imissueheader> list = imissueheaderService.LIM13(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = imissueheaderService.LIM13(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Imissueheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LIM14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Imadjheader> searchLIM14(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Imadjheader> list = imadjheaderService.LIM14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = imadjheaderService.LIM14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Imadjheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSO10/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opquotheader> searchLSO10(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opquotheader> list = opquotheaderService.LSO10(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = opquotheaderService.LSO10(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Opquotheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSO12/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opreqheader> searchLSO12(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opreqheader> list = opreqheaderService.LSO12(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = opreqheaderService.LSO12(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Opreqheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSO14/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opordheader> searchLSO14(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opordheader> list = opordheaderService.LSO14(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = opordheaderService.LSO14(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Opordheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSO17/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opdoheader> searchLSO17(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opdoheader> list = opdoheaderService.LSO17(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = opdoheaderService.LSO17(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Opdoheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSO19/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opcrnheader> searchLSO19(@PathVariable int suffix) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opcrnheader> list = opcrnheaderService.LSO19(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);
		int totalRows = opcrnheaderService.LSO19(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix);

		DatatableResponseHelper<Opcrnheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/LSP11/{suffix}")
	public @ResponseBody DatatableResponseHelper<Opships> LSP11(@PathVariable int suffix, @RequestParam(required = false) String dependentParam) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opships> list = opshipsService.LSP11(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);
		int totalRows = opshipsService.LSP11(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), suffix, dependentParam);

		DatatableResponseHelper<Opships> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@GetMapping("/item/{hint}")
	public @ResponseBody List<SearchSuggestResult> getItems(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Caitem> cacusList = caitemRepo.searchAllItems(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXitem().toString(), c.getXitem() + " - " + c.getXdesc())));
		return list;
	}

	@GetMapping("/customer/{hint}")
	public @ResponseBody List<SearchSuggestResult> getCustomers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Cacus> cacusList = cacusRepo.searchAllCustomers(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus().toString(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}

	@GetMapping("/customer-active/{hint}")
	public @ResponseBody List<SearchSuggestResult> getActiveCustomers(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Cacus> cacusList = cacusRepo.searchActiveCustomers(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXcus().toString(), c.getXcus() + " - " + c.getXorg())));
		return list;
	}

	@GetMapping("/store/{hint}")
	public @ResponseBody List<SearchSuggestResult> getStores(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Xwhs> cacusList = xwhsRepo.searchStores(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		cacusList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXwh().toString(), c.getXwh() + " - " + c.getXname())));
		return list;
	}

	@GetMapping("/staff/{hint}")
	public @ResponseBody List<SearchSuggestResult> getStaffs(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Pdmst> staffList = pdmstRepo.searchStaff(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		staffList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXstaff().toString(), c.getXstaff() + " - " + c.getXname())));
		return list;
	}

	@GetMapping("/area/{hint}")
	public @ResponseBody List<SearchSuggestResult> getAreas(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Oparea> staffList = opareaRepo.searchArea(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		staffList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXarea().toString(), c.getXarea() + " - " + c.getXname())));
		return list;
	}

	@GetMapping("/bank/{hint}")
	public @ResponseBody List<SearchSuggestResult> getBanks(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Cabank> staffList = cabankRepo.searchBanks(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		staffList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXbank().toString(), c.getXbank() + " - " + c.getXname())));
		return list;
	}

	@GetMapping("/bank-active/{hint}")
	public @ResponseBody List<SearchSuggestResult> getActiveBanks(@PathVariable String hint){
		if(StringUtils.isBlank(hint)) return Collections.emptyList();

		List<Cabank> staffList = cabankRepo.searchActiveBanks(hint, sessionManager.getBusinessId());

		List<SearchSuggestResult> list = new ArrayList<>();
		staffList.stream().forEach(c -> list.add(new SearchSuggestResult(c.getXbank().toString(), c.getXbank() + " - " + c.getXname())));
		return list;
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
