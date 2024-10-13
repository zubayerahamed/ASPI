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

	public static final Map<String, String> RP01 = new HashMap<>();
	public static final Map<String, String> RP02 = new HashMap<>();
	public static final Map<String, String> RP03 = new HashMap<>();
	public static final Map<String, String> RP04 = new HashMap<>();
	public static final Map<String, String> RP05 = new HashMap<>();
	public static final Map<String, String> RP06 = new HashMap<>();
	public static final Map<String, String> RP07 = new HashMap<>();
	public static final Map<String, String> RP08 = new HashMap<>();
	public static final Map<String, String> RP09 = new HashMap<>();
	public static final Map<String, String> RP10 = new HashMap<>();
	public static final Map<String, String> RP11 = new HashMap<>();
	public static final Map<String, String> RP12 = new HashMap<>();
	public static final Map<String, String> RP13 = new HashMap<>();
	public static final Map<String, String> RP14 = new HashMap<>();
	public static final Map<String, String> RP15 = new HashMap<>();
	public static final Map<String, String> RP16 = new HashMap<>();
	public static final Map<String, String> RP17 = new HashMap<>();

	static {

		voucher.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		voucher.put("param2", "xvoucher|" + ReportParamDataType.INTEGER.name());

		RP01.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP01.put("param2", "xstaff|" + ReportParamDataType.INTEGER.name());
		RP01.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP02.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP02.put("param2", "xprofile|" + ReportParamDataType.STRING.name());
		RP02.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP03.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP03.put("param2", "xagcode|" + ReportParamDataType.INTEGER.name());
		RP03.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP04.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP04.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP05.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP05.put("param2", "xtype|" + ReportParamDataType.STRING.name());
		RP05.put("param3", "xacc|" + ReportParamDataType.INTEGER.name());
		RP05.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP06.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP06.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP06.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP06.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP06.put("param5", "xtype|" + ReportParamDataType.STRING.name());
		RP06.put("param6", "xvtype|" + ReportParamDataType.STRING.name());
		RP06.put("param7", "xstatusjv|" + ReportParamDataType.STRING.name());
		RP06.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP07.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP07.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP07.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP07.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP07.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		RP07.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		RP07.put("param7", "xyear|" + ReportParamDataType.INTEGER.name());
		RP07.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP08.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP08.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP08.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP08.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP08.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		RP08.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		RP08.put("param7", "xyear|" + ReportParamDataType.INTEGER.name());
		RP08.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP09.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP09.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP09.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP09.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP09.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP09.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP10.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP10.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP10.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP10.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP10.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP10.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP11.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP11.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP11.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP11.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP11.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP11.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP12.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP12.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP12.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP12.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP12.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP12.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP13.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP13.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP13.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP13.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP13.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP13.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP14.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP14.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP14.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP14.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP14.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		RP14.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP15.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP15.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP15.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP15.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP15.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		RP15.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		RP15.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP16.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP16.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP16.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP16.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP16.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		RP17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		RP17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		RP17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		RP17.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		RP17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
	}
}
