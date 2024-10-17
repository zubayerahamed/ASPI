package com.zayaanit.enums;

import java.util.Map;

import com.zayaanit.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// ON SCREEN REPORT
	voucher("FA15", "Voucher", "voucher.rpt", ReportParamMap.voucher, "Y", false),

	// REPORT MODULES REPORT
	RP01("RP01", "User Listing Report", "RP01.rpt", ReportParamMap.RP01, "Y", false),
	RP02("RP02", "Profile Wise Access Report", "RP02.rpt", ReportParamMap.RP02, "Y", false),
	RP03("RP03", "Chart of Account Detail", "RP03.rpt", ReportParamMap.RP03, "Y", false),
	RP04("RP04", "Chart of Account Summary", "RP04.rpt", ReportParamMap.RP04, "Y", false),
	RP05("RP05", "Sub Account Report", "RP05.rpt", ReportParamMap.RP05, "Y", false),
	RP06("RP06", "General Journal", "RP06.rpt", ReportParamMap.RP06, "Y", false),
	RP07("RP07", "Account Ledger", "RP07.rpt", ReportParamMap.RP07, "Y", false),
	RP08("RP08", "Cash/Bank Book", "RP08.rpt", ReportParamMap.RP08, "Y", false),
	RP09("RP09", "Trail Balance Detail", "RP09.rpt", ReportParamMap.RP09, "Y", false),
	RP10("RP10", "Trail Balance Summary", "RP10.rpt", ReportParamMap.RP10, "Y", false),
	RP11("RP11", "Profit & Loss Detail", "RP11.rpt", ReportParamMap.RP11, "Y", false),
	RP12("RP12", "Profit & Loss Statement", "RP12.rpt", ReportParamMap.RP12, "Y", false),
	RP13("RP13", "Balance Sheet Detail", "RP13.rpt", ReportParamMap.RP13, "Y", false),
	RP14("RP14", "Balance Sheet Summary", "RP14.rpt", ReportParamMap.RP14, "Y", false),
	RP15("RP15", "Cross Year Account Ledger", "RP15.rpt", ReportParamMap.RP15, "Y", false),
	RP16("RP16", "Cross Year Trial Balance", "RP16.rpt", ReportParamMap.RP16, "Y", false),
	RP17("RP16", "Cross Year Profit & Loss", "RP17.rpt", ReportParamMap.RP17, "Y", false),
	RP18("RP18", "Sub Account Ledger Detail", "RP18.rpt", ReportParamMap.RP18, "Y", false),
	RP19("RP19", "Sub Account Ledger Summary", "RP19.rpt", ReportParamMap.RP19, "Y", false);

	private String group;
	private String description;
	private String fileName;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean enabledFop;

	private ReportMenu(String group, String des, String fileName, Map<String, String> paramMap, String defaultAccess, boolean enabledFop) {
		this.group = group;
		this.description = des;
		this.fileName = fileName;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.enabledFop = enabledFop;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getGroup() {
		return this.group;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	public boolean isEnabledFop() {
		return this.enabledFop;
	}
}
