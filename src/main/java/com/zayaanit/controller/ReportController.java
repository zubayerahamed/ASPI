package com.zayaanit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zayaanit.entity.Xcodes;
import com.zayaanit.entity.Xscreendetail;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.ReportMenu;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;
import com.zayaanit.repository.XscreendetailRepo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
@Controller
@RequestMapping("/{rptcode}")
public class ReportController extends AbstractReportController{

	private String pageTitle = null;
	private String screenCode = null;

	@Autowired private XscreendetailRepo xscreendetailRepo;

	@Override
	protected String screenCode() {
		return this.screenCode;
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), this.screenCode));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@PathVariable String rptcode, HttpServletRequest request, Model model) {
		this.screenCode = rptcode;
		model.addAttribute("isFavorite", isFavorite());

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), rptcode));
		if(op.isPresent()) this.pageTitle = op.get().getXtitle();

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("screenCode", rptcode);
		model.addAttribute("xtitle", pageTitle);

		ReportMenu rm = null;
		try {
			rm = ReportMenu.valueOf(rptcode);
		} catch (Exception e) {
			log.error(ERROR, e.getMessage(), e);
			if(!isAjaxRequest(request)) {
				return "redirect:/";
			}

			model.addAttribute("error", "Page Not found");
			model.addAttribute("status", "404");
			return "pages/404";
		}

		List<FormFieldBuilder> fields = null;
		List<Xscreendetail> fieldsList = xscreendetailRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), rptcode);
		if(fieldsList != null && !fieldsList.isEmpty()) {
			fields = getReportFields(fieldsList);
		} else {
			fields = getReportFieldService(rm).getReportFields();
		}

		model.addAttribute("fieldsList", fields);
		model.addAttribute("reportFound", true);
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", rm.getDescription());
		model.addAttribute("reportCode", rm.name());

		return "pages/RP/RP";
	}

	private List<FormFieldBuilder> getReportFields(List<Xscreendetail> details){
		details.sort(Comparator.comparing(Xscreendetail::getXseqn));

		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		for(Xscreendetail detail : details) {
			if("DATE".equalsIgnoreCase(detail.getXtype())) {
				fieldsList.add(FormFieldBuilder.generateDateField(detail.getXseqn(), detail.getXisstartdate(), detail.getXlabel(), new Date(), detail.getXisrequired()));
			} else if ("SEARCH_ADVANCED".equalsIgnoreCase(detail.getXtype())) {
				fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(detail.getXseqn(), detail.getXlabel(), "/search/table/"+ detail.getXsearchcode() +"/"+ detail.getXsearchsuffix() +"?hint=", detail.getXdefaultvalue(), detail.getXisrequired()));
			} else if ("SELECT2".equalsIgnoreCase(detail.getXtype()) || "DROPDOWN".equalsIgnoreCase(detail.getXtype())) {
				List<DropdownOption> options = new ArrayList<>();
				options.add(new DropdownOption("", "-- Select --"));

				if(StringUtils.isNotBlank(detail.getXoptions())) {
					String optionsString = detail.getXoptions().trim();
					String[] optionsArr = optionsString.split("\\|");
					Arrays.asList(optionsArr).stream().forEach(opt -> {
						String[] optionArr = opt.split("\\:");
						options.add(new DropdownOption(optionArr[0], optionArr[1]));
					});
				}

				if(StringUtils.isNotBlank(detail.getXoptionsquery())) {
//					List<Xcodes> groups = xcodesRepo.findAllByXtypeAndZactiveAndZid("Item Group", Boolean.TRUE, sessionManager.getBusinessId());
//					groups.forEach(f -> {
//						options.add(new DropdownOption(f.getXcode(), f.getXcode()));
//					});
				}

				fieldsList.add(FormFieldBuilder.generateDropdownField(detail.getXseqn(), detail.getXlabel(), options, detail.getXdefaultvalue(), detail.getXisrequired()));
			}
		}

		return fieldsList;
	}

}
