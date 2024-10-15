package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.Zbusiness;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.exceptions.UnauthorizedException;
import com.zayaanit.model.QueryWindow;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.repository.ZbusinessRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/SA15")
public class SA15 extends KitController {

	@Autowired private ZbusinessRepo zbusinessRepo;
	@Autowired protected JdbcTemplate jdbcTemplate;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA15";
	}

	@Override
	protected String pageTitle() {
		if (this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA15"));
		if (!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) throws UnauthorizedException {
		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorize Access");
		}

		QueryWindow qw = new QueryWindow();
		model.addAttribute("qw", qw);

		if (isAjaxRequest(request)) {
			return "pages/SA15/SA15-fragments::main-form";
		}

		return "pages/SA15/SA15";
	}

	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(QueryWindow qw) {

		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Unauthorized Access");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(qw.getXpassword())) {
			responseHelper.setErrorStatusAndMessage("Security Key Required");
			return responseHelper.getResponse();
		}

		Optional<Zbusiness> businessOp = zbusinessRepo.findById(100100);   // Fixed zbusiness password check
		if(!businessOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Business not found");
			return responseHelper.getResponse();
		}

//		if(!qw.getXpassword().equals(businessOp.get().getZpassword())) {
//			responseHelper.setErrorStatusAndMessage("Invalid Security Key");
//			return responseHelper.getResponse();
//		}

		if(StringUtils.isBlank(qw.getStatement())) {
			responseHelper.setErrorStatusAndMessage("Statement is empty");
			return responseHelper.getResponse();
		}

		String sql = qw.getStatement().trim();

		if("UPDATE QUERY".equalsIgnoreCase(qw.getType()) || "DELETE QUERY".equalsIgnoreCase(qw.getType()) || "INSERT QUERY".equalsIgnoreCase(qw.getType())) {
			try {
				int effectedRows = jdbcTemplate.update(sql);
				responseHelper.setSuccessStatusAndMessage("Process Successfull. Number of effected rows : " + effectedRows);
				return responseHelper.getResponse();
			} catch (Exception e) {
				responseHelper.setErrorStatusAndMessage("Process Failed. Reason : " + e.getMessage());
				return responseHelper.getResponse();
			}
		} 

		if(sessionManager.getFromMap("QW_SQL") != null) {
			sessionManager.removeFromMap("QW_SQL");
		}

		sessionManager.addToMap("QW_SQL", sql);

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("detail-table-container", "/SA15/detail-table"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Process Successfully");
		responseHelper.setDisplayMessage(false);
		return responseHelper.getResponse();
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam(required = false) String clear, Model model) throws UnauthorizedException {
		if("CLEAR".equals(clear)) {
			model.addAttribute("detailSection", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		model.addAttribute("detailSection", true);

		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			model.addAttribute("dataFound", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		if(sessionManager.getFromMap("QW_SQL") == null) {
			model.addAttribute("dataFound", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}

		String sql = (String) sessionManager.getFromMap("QW_SQL");
		if (sql.endsWith(";")) sql = sql.substring(0, sql.length() - 1);
		if(!sql.toLowerCase().startsWith("select top")) {
			sql = "SELECT TOP 1000 " + sql.substring("select ".length());
		}
		sessionManager.removeFromMap("QW_SQL");

		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		if(resultList == null || resultList.isEmpty()) {
			model.addAttribute("dataFound", false);
			return "pages/SA15/SA15-fragments::detail-table";
		}
		model.addAttribute("dataFound", true);

		int totalColumns = resultList.get(0).size();
		List<String> columns = new ArrayList<>();
		resultList.get(0).entrySet().stream().forEach(f -> {
			columns.add(f.getKey());
		});

		model.addAttribute("totalColumns", totalColumns);
		model.addAttribute("columns", columns);
		model.addAttribute("data", resultList);
		return "pages/SA15/SA15-fragments::detail-table";
	}
}
