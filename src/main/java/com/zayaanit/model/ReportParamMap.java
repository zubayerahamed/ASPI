package com.zayaanit.model;

import java.util.HashMap;
import java.util.Map;

import com.zayaanit.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {

	public static final Map<String, String> voucher = new HashMap<>();

	public static final Map<String, String> R101 = new HashMap<>();
	public static final Map<String, String> R102 = new HashMap<>();
	public static final Map<String, String> R201 = new HashMap<>();
	public static final Map<String, String> R202 = new HashMap<>();
	public static final Map<String, String> R203 = new HashMap<>();
	public static final Map<String, String> R204 = new HashMap<>();
	public static final Map<String, String> R205 = new HashMap<>();
	public static final Map<String, String> R206 = new HashMap<>();
	public static final Map<String, String> R207 = new HashMap<>();
	public static final Map<String, String> R208 = new HashMap<>();
	public static final Map<String, String> R209 = new HashMap<>();
	public static final Map<String, String> R210 = new HashMap<>();
	public static final Map<String, String> R211 = new HashMap<>();
	public static final Map<String, String> R212 = new HashMap<>();
	public static final Map<String, String> R213 = new HashMap<>();
	public static final Map<String, String> R214 = new HashMap<>();
	public static final Map<String, String> R215 = new HashMap<>();
	public static final Map<String, String> R216 = new HashMap<>();
	public static final Map<String, String> R217 = new HashMap<>();

	static {

		voucher.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		voucher.put("param2", "xvoucher|" + ReportParamDataType.INTEGER.name());

		R101.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R101.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R101.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R102.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R102.put("param2", "xprofile|" + ReportParamDataType.STRING.name());
		R102.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R102.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R201.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R201.put("param2", "xagcode|" + ReportParamDataType.INTEGER.name());
		R201.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R201.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R202.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R202.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R202.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R203.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R203.put("param2", "xtype|" + ReportParamDataType.STRING.name());
		R203.put("param3", "xacc|" + ReportParamDataType.INTEGER.name());
		R203.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R203.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R204.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R204.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R204.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R204.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R204.put("param5", "xtype|" + ReportParamDataType.STRING.name());
		R204.put("param6", "xvtype|" + ReportParamDataType.STRING.name());
		R204.put("param7", "xstatusjv|" + ReportParamDataType.STRING.name());
		R204.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R204.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R205.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R205.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R205.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R205.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R205.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R205.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		R205.put("param7", "xyear|" + ReportParamDataType.INTEGER.name());
		R205.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R205.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R206.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R206.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R206.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R206.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R206.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R206.put("param6", "xyear|" + ReportParamDataType.INTEGER.name());
		R206.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R206.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R207.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R207.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R207.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R207.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R207.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R207.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R207.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R208.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R208.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R208.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R208.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R208.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R208.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R208.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R209.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R209.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R209.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R209.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R209.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R209.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R209.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R210.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R210.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R210.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R210.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R210.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R210.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R210.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R211.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R211.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R211.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R211.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R211.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R211.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R211.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R212.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R212.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R212.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R212.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R212.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R212.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R212.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R213.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R213.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R213.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R213.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R213.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R213.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		R213.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R213.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R214.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R214.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R214.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R214.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R214.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R214.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R215.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R215.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R215.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R215.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R215.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R215.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R216.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R216.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R216.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R216.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R216.put("param5", "xsub|" + ReportParamDataType.INTEGER.name());
		R216.put("param6", "xacc|" + ReportParamDataType.INTEGER.name());
		R216.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R216.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R217.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R217.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R217.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R217.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R217.put("param5", "xtype|" + ReportParamDataType.STRING.name());
		R217.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R217.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
	}
}
