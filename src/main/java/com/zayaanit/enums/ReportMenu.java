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
	R101("R101", "User Listing Report", "R101.rpt", ReportParamMap.R101, "Y", false),
	R102("R102", "Profile Wise Access Report", "R102.rpt", ReportParamMap.R102, "Y", false),
	R201("R201", "Chart of Account Detail", "R201.rpt", ReportParamMap.R201, "Y", false),
	R202("R202", "Chart of Account Summary", "R202.rpt", ReportParamMap.R202, "Y", false),
	R203("R203", "Sub Account Report", "R203.rpt", ReportParamMap.R203, "Y", false),
	R204("R204", "General Journal", "R204.rpt", ReportParamMap.R204, "Y", false),
	R205("R205", "Account Ledger", "R205.rpt", ReportParamMap.R205, "Y", false),
	R206("R206", "Cash/Bank Book", "R206.rpt", ReportParamMap.R206, "Y", false),
	R207("R207", "Trail Balance Detail", "R207.rpt", ReportParamMap.R207, "Y", false),
	R208("R208", "Trail Balance Summary", "R208.rpt", ReportParamMap.R208, "Y", false),
	R209("R209", "Profit & Loss Detail", "R209.rpt", ReportParamMap.R209, "Y", false),
	R210("R210", "Profit & Loss Statement", "R210.rpt", ReportParamMap.R210, "Y", false),
	R211("R211", "Balance Sheet Detail", "R211.rpt", ReportParamMap.R211, "Y", false),
	R212("R212", "Balance Sheet Summary", "R212.rpt", ReportParamMap.R212, "Y", false),
	R213("R213", "Cross Year Account Ledger", "R213.rpt", ReportParamMap.R213, "Y", false),
	R214("R214", "Cross Year Trial Balance", "R214.rpt", ReportParamMap.R214, "Y", false),
	R215("R215", "Cross Year Profit & Loss", "R215.rpt", ReportParamMap.R215, "Y", false),
	R216("R216", "Sub Account Ledger Detail", "R216.rpt", ReportParamMap.R216, "Y", false),
	R217("R217", "Sub Account Ledger Summary", "R217.rpt", ReportParamMap.R217, "Y", false);

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
