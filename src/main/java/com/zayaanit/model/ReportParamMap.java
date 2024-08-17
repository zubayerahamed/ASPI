package com.zayaanit.model;

import java.util.HashMap;
import java.util.Map;

import com.zayaanit.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {
	
	public static final Map<String, String> imtor = new HashMap<>();
	public static final Map<String, String> imtorreq = new HashMap<>();
	public static final Map<String, String> imrcv = new HashMap<>();
	public static final Map<String, String> imiss = new HashMap<>();
	public static final Map<String, String> imadj = new HashMap<>();
	public static final Map<String, String> opreq = new HashMap<>();
	public static final Map<String, String> opreqtoord = new HashMap<>();
	public static final Map<String, String> opdolist = new HashMap<>();
	public static final Map<String, String> opord = new HashMap<>();
	public static final Map<String, String> opordprev = new HashMap<>();
	public static final Map<String, String> opordinv = new HashMap<>();
	public static final Map<String, String> opshipd = new HashMap<>();
	public static final Map<String, String> opships = new HashMap<>();
	public static final Map<String, String> opshipg = new HashMap<>();

	public static final Map<String, String> opdoinv = new HashMap<>();
	public static final Map<String, String> opdoinvdv = new HashMap<>();
	public static final Map<String, String> opdoinvsv = new HashMap<>();
	public static final Map<String, String> opdocln = new HashMap<>();
	public static final Map<String, String> opdoinvd = new HashMap<>();
	public static final Map<String, String> opdoclnd = new HashMap<>();
	public static final Map<String, String> doprev = new HashMap<>();

	public static final Map<String, String> opcrn = new HashMap<>();

	public static final Map<String, String> armr = new HashMap<>();
	public static final Map<String, String> arledd = new HashMap<>();
	public static final Map<String, String> opundel = new HashMap<>();

	public static final Map<String, String> opdodtmis = new HashMap<>();
	public static final Map<String, String> opdosmmis = new HashMap<>();
	public static final Map<String, String> imstockmis = new HashMap<>();
	public static final Map<String, String> imleddtmis = new HashMap<>();
	public static final Map<String, String> arledsmtrmis = new HashMap<>();
	public static final Map<String, String> cacusarmis = new HashMap<>();
	public static final Map<String, String> opshipsdt = new HashMap<>();
	public static final Map<String, String> opshipssm = new HashMap<>();

	public static final Map<String, String> xusers = new HashMap<>();
	public static final Map<String, String> pdmst = new HashMap<>();
	public static final Map<String, String> caitem = new HashMap<>();
	public static final Map<String, String> cacusar = new HashMap<>();
	public static final Map<String, String> cabank = new HashMap<>();
	public static final Map<String, String> opvhls = new HashMap<>();

	public static final Map<String, String> arleddt = new HashMap<>();
	public static final Map<String, String> arledsm = new HashMap<>();
	public static final Map<String, String> armrdt = new HashMap<>();
	public static final Map<String, String> armrbnkdt = new HashMap<>();
	public static final Map<String, String> aradjdt = new HashMap<>();
	public static final Map<String, String> arcollec = new HashMap<>();
	public static final Map<String, String> arledsmtr = new HashMap<>();

	public static final Map<String, String> opreqdt = new HashMap<>();
	public static final Map<String, String> opreqsm = new HashMap<>();
	public static final Map<String, String> oporddt = new HashMap<>();
	public static final Map<String, String> opordsm = new HashMap<>();
	public static final Map<String, String> opdodt = new HashMap<>();
	public static final Map<String, String> opdosmcat = new HashMap<>();
	public static final Map<String, String> opdosm = new HashMap<>();
	public static final Map<String, String> opcrndt = new HashMap<>();
	public static final Map<String, String> opcrnsm = new HashMap<>();
	public static final Map<String, String> opundeldt = new HashMap<>();
	public static final Map<String, String> opundelsm = new HashMap<>();
	public static final Map<String, String> opundelsmtr = new HashMap<>();
	public static final Map<String, String> opundelcus = new HashMap<>();

	public static final Map<String, String> imtordt = new HashMap<>();
	public static final Map<String, String> imrcvdt = new HashMap<>();
	public static final Map<String, String> imissdt = new HashMap<>();
	public static final Map<String, String> imadjdt = new HashMap<>();
	public static final Map<String, String> imstock = new HashMap<>();
	public static final Map<String, String> imleddt = new HashMap<>();
	public static final Map<String, String> imledsm = new HashMap<>();
	public static final Map<String, String> imledsmmis = new HashMap<>();
	public static final Map<String, String> imagesm = new HashMap<>();
	public static final Map<String, String> imagedt = new HashMap<>();
	public static final Map<String, String> imstocklow = new HashMap<>();
	public static final Map<String, String> opundelage = new HashMap<>();
	public static final Map<String, String> imundelstock = new HashMap<>();

	public static final Map<String, String> opreqdtff = new HashMap<>();
	public static final Map<String, String> opreqsmff = new HashMap<>();
	public static final Map<String, String> oporddtff = new HashMap<>();
	public static final Map<String, String> opordsmff = new HashMap<>();
	public static final Map<String, String> opdodtff = new HashMap<>();
	public static final Map<String, String> opdosmcatff = new HashMap<>();
	public static final Map<String, String> opdosmff = new HashMap<>();
	public static final Map<String, String> opcrndtff = new HashMap<>();
	public static final Map<String, String> opcrnsmff = new HashMap<>();
	public static final Map<String, String> opundeldtff = new HashMap<>();
	public static final Map<String, String> opundelsmff = new HashMap<>();
	public static final Map<String, String> opundelcusff = new HashMap<>();
	public static final Map<String, String> armrdtff = new HashMap<>();
	public static final Map<String, String> aradjdtff = new HashMap<>();
	public static final Map<String, String> arledsmff = new HashMap<>();
	public static final Map<String, String> caitemff = new HashMap<>();
	public static final Map<String, String> caitemffrp12 = new HashMap<>();

	public static final Map<String, String> imtordtrp17 = new HashMap<>();
	public static final Map<String, String> imrcvdtrp17 = new HashMap<>();
	public static final Map<String, String> imissdtrp17 = new HashMap<>();
	public static final Map<String, String> imadjdtrp17 = new HashMap<>();
	public static final Map<String, String> imstockrp17 = new HashMap<>();
	public static final Map<String, String> imleddtrp17 = new HashMap<>();
	public static final Map<String, String> imledsmrp17 = new HashMap<>();
	public static final Map<String, String> imledsmmisrp17 = new HashMap<>();
	public static final Map<String, String> imagesmrp17 = new HashMap<>();
	public static final Map<String, String> imagedtrp17 = new HashMap<>();
	public static final Map<String, String> opundelagerp17 = new HashMap<>();
	public static final Map<String, String> imundelstockrp17 = new HashMap<>();

	static {
		arledd.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arledd.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arledd.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arledd.put("param4", "xcus|" + ReportParamDataType.INTEGER.name());

		opundel.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundel.put("param2", "xcus|" + ReportParamDataType.INTEGER.name());

		imtor.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtor.put("param2", "xtornum|" + ReportParamDataType.INTEGER.name());

		imtorreq.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtorreq.put("param2", "xtornum|" + ReportParamDataType.INTEGER.name());

		imrcv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imrcv.put("param2", "xrcvnum|" + ReportParamDataType.INTEGER.name());

		imiss.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imiss.put("param2", "xissuenum|" + ReportParamDataType.INTEGER.name());

		imadj.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imadj.put("param2", "xadjnum|" + ReportParamDataType.INTEGER.name());

		opreq.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreq.put("param2", "xdoreqnum|" + ReportParamDataType.INTEGER.name());

		opreqtoord.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreqtoord.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opreqtoord.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opreqtoord.put("param4", "xitemtype|" + ReportParamDataType.STRING.name());
		opreqtoord.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opreqtoord.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opreqtoord.put("param7", "xstaff|" + ReportParamDataType.INTEGER.name());
		opreqtoord.put("param8", "xstatusreq|" + ReportParamDataType.STRING.name());

		opdolist.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdolist.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdolist.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdolist.put("param4", "xitemtype|" + ReportParamDataType.STRING.name());
		opdolist.put("param5", "xarea|" + ReportParamDataType.INTEGER.name());
		opdolist.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdolist.put("param7", "xwh|" + ReportParamDataType.INTEGER.name());
		opdolist.put("param8", "xstaff|" + ReportParamDataType.INTEGER.name());

		opord.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opord.put("param2", "xordernum|" + ReportParamDataType.INTEGER.name());

		opordprev.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opordprev.put("param2", "xordernum|" + ReportParamDataType.INTEGER.name());

		opordinv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opordinv.put("param2", "xordernum|" + ReportParamDataType.INTEGER.name());

		opshipd.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opshipd.put("param2", "xshipment|" + ReportParamDataType.INTEGER.name());

		opships.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opships.put("param2", "xshipment|" + ReportParamDataType.INTEGER.name());

		opshipg.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opshipg.put("param2", "xshipment|" + ReportParamDataType.INTEGER.name());

		opdoinv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoinv.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdoinvdv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoinvdv.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdoinvsv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoinvsv.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdocln.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdocln.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdoinvd.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoinvd.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdoclnd.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoclnd.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		doprev.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		doprev.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opcrn.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrn.put("param2", "xcrnnum|" + ReportParamDataType.INTEGER.name());

		armr.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		armr.put("param2", "xtrnnum|" + ReportParamDataType.INTEGER.name());

		opdodtmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdodtmis.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdodtmis.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdodtmis.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opdodtmis.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opdodtmis.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdodtmis.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opdodtmis.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		opdodtmis.put("param9", "xdornum|" + ReportParamDataType.INTEGER.name());
		opdodtmis.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		opdodtmis.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		opdodtmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdosmmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdosmmis.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdosmmis.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdosmmis.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opdosmmis.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opdosmmis.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdosmmis.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opdosmmis.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		opdosmmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imstockmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imstockmis.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imstockmis.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imstockmis.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		imstockmis.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		imstockmis.put("param6", "xsubcat|" + ReportParamDataType.STRING.name());
		imstockmis.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		imstockmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imleddtmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imleddtmis.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imleddtmis.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imleddtmis.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		imleddtmis.put("param5", "xitem|" + ReportParamDataType.INTEGER.name());
		imleddtmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arledsmtrmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arledsmtrmis.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arledsmtrmis.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arledsmtrmis.put("param4", "xsunit|" + ReportParamDataType.INTEGER.name());
		arledsmtrmis.put("param5", "xdivision|" + ReportParamDataType.INTEGER.name());
		arledsmtrmis.put("param6", "xsdivision|" + ReportParamDataType.INTEGER.name());
		arledsmtrmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		cacusarmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		cacusarmis.put("param2", "xsunit|" + ReportParamDataType.INTEGER.name());
		cacusarmis.put("param3", "xdivision|" + ReportParamDataType.INTEGER.name());
		cacusarmis.put("param4", "xsdivision|" + ReportParamDataType.INTEGER.name());
		cacusarmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opshipsdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opshipsdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opshipsdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opshipsdt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opshipsdt.put("param5", "xshipment|" + ReportParamDataType.INTEGER.name());
		opshipsdt.put("param6", "xtypeship|" + ReportParamDataType.STRING.name());
		opshipsdt.put("param7", "xvhl|" + ReportParamDataType.INTEGER.name());
		opshipsdt.put("param8", "xstatus|" + ReportParamDataType.STRING.name());
		opshipsdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opshipssm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opshipssm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opshipssm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opshipssm.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opshipssm.put("param5", "xshipment|" + ReportParamDataType.INTEGER.name());
		opshipssm.put("param6", "xtypeship|" + ReportParamDataType.STRING.name());
		opshipssm.put("param7", "xvhl|" + ReportParamDataType.INTEGER.name());
		opshipssm.put("param8", "xstatus|" + ReportParamDataType.STRING.name());
		opshipssm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		xusers.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		xusers.put("param2", "xstaff|" + ReportParamDataType.INTEGER.name());
		xusers.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		pdmst.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		pdmst.put("param2", "xdesignation|" + ReportParamDataType.STRING.name());
		pdmst.put("param3", "xdepartment|" + ReportParamDataType.STRING.name());
		pdmst.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		caitem.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		caitem.put("param2", "xgitem|" + ReportParamDataType.STRING.name());
		caitem.put("param3", "xcatitem|" + ReportParamDataType.STRING.name());
		caitem.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		cacusar.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		cacusar.put("param2", "xcatcus|" + ReportParamDataType.STRING.name());
		cacusar.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		cabank.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		cabank.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opvhls.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opvhls.put("param2", "xtypeowner|" + ReportParamDataType.STRING.name());
		opvhls.put("param3", "xtypevhl|" + ReportParamDataType.STRING.name());
		opvhls.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arleddt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arleddt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arleddt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arleddt.put("param4", "xcus|" + ReportParamDataType.INTEGER.name());
		arleddt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arledsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arledsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arledsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arledsm.put("param4", "xcatcus|" + ReportParamDataType.STRING.name());
		arledsm.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		arledsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		armrdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		armrdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		armrdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		armrdt.put("param4", "xbank|" + ReportParamDataType.INTEGER.name());
		armrdt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		armrdt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		armrdt.put("param7", "xstaff|" + ReportParamDataType.INTEGER.name());
		armrdt.put("param8", "xstatus|" + ReportParamDataType.STRING.name());
		armrdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		armrbnkdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		armrbnkdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		armrbnkdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		armrbnkdt.put("param4", "xbank|" + ReportParamDataType.INTEGER.name());
		armrbnkdt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		armrbnkdt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		armrbnkdt.put("param7", "xstaff|" + ReportParamDataType.INTEGER.name());
		armrbnkdt.put("param8", "xstatus|" + ReportParamDataType.STRING.name());
		armrbnkdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		aradjdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		aradjdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		aradjdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		aradjdt.put("param4", "xcatcus|" + ReportParamDataType.STRING.name());
		aradjdt.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		aradjdt.put("param6", "xstaff|" + ReportParamDataType.INTEGER.name());
		aradjdt.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		aradjdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arcollec.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arcollec.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arcollec.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arcollec.put("param4", "xcatcus|" + ReportParamDataType.STRING.name());
		arcollec.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		arcollec.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arledsmtr.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arledsmtr.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arledsmtr.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arledsmtr.put("param4", "xsunit|" + ReportParamDataType.INTEGER.name());
		arledsmtr.put("param5", "xdivision|" + ReportParamDataType.INTEGER.name());
		arledsmtr.put("param6", "xsdivision|" + ReportParamDataType.INTEGER.name());
		arledsmtr.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opreqdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreqdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opreqdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opreqdt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opreqdt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opreqdt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opreqdt.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opreqdt.put("param8", "xstatusreq|" + ReportParamDataType.STRING.name());
		opreqdt.put("param9", "xdoreqnum|" + ReportParamDataType.INTEGER.name());
		opreqdt.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		opreqdt.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		opreqdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opreqsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreqsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opreqsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opreqsm.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opreqsm.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opreqsm.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opreqsm.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opreqsm.put("param8", "xstatusreq|" + ReportParamDataType.STRING.name());
		opreqsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		oporddt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		oporddt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		oporddt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		oporddt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		oporddt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		oporddt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		oporddt.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		oporddt.put("param8", "xstatusord|" + ReportParamDataType.STRING.name());
		oporddt.put("param9", "xordernum|" + ReportParamDataType.INTEGER.name());
		oporddt.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		oporddt.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		oporddt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opordsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opordsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opordsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opordsm.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opordsm.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opordsm.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opordsm.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opordsm.put("param8", "xstatusord|" + ReportParamDataType.STRING.name());
		opordsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdodt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdodt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdodt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdodt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opdodt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opdodt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdodt.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		opdodt.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		opdodt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdosmcat.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdosmcat.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdosmcat.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdosmcat.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opdosmcat.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opdosmcat.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdosmcat.put("param7", "xgitem|" + ReportParamDataType.STRING.name());
		opdosmcat.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdosm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdosm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdosm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdosm.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opdosm.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opdosm.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opdosm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opcrndt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrndt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opcrndt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opcrndt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opcrndt.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opcrndt.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opcrndt.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		opcrndt.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		opcrndt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opcrnsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrnsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opcrnsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opcrnsm.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		opcrnsm.put("param5", "xcatcus|" + ReportParamDataType.STRING.name());
		opcrnsm.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opcrnsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundeldt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundeldt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundeldt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundeldt.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundeldt.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		opundeldt.put("param6", "xcatcus|" + ReportParamDataType.STRING.name());
		opundeldt.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		opundeldt.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundeldt.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundeldt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundelsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundelsm.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundelsm.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelsm.put("param6", "xcatcus|" + ReportParamDataType.STRING.name());
		opundelsm.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelsm.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundelsm.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundelsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelsmtr.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundelsmtr.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundelsmtr.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundelsmtr.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param6", "xsunit|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param7", "xdivision|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param8", "xsdivision|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param9", "xarea|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param10", "xterritory|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("param11", "xcatitem|" + ReportParamDataType.STRING.name());
		opundelsmtr.put("param12", "xitem|" + ReportParamDataType.INTEGER.name());
		opundelsmtr.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelcus.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelcus.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundelcus.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundelcus.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundelcus.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelcus.put("param6", "xarea|" + ReportParamDataType.INTEGER.name());
		opundelcus.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelcus.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundelcus.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundelcus.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imtordt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtordt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imtordt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imtordt.put("param4", "xfwh|" + ReportParamDataType.INTEGER.name());
		imtordt.put("param5", "xtwh|" + ReportParamDataType.INTEGER.name());
		imtordt.put("param6", "xstatus|" + ReportParamDataType.STRING.name());
		imtordt.put("param7", "xstatusim|" + ReportParamDataType.STRING.name());
		imtordt.put("param8", "xtornum|" + ReportParamDataType.INTEGER.name());
		imtordt.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		imtordt.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		imtordt.put("param11", "xsubcat|" + ReportParamDataType.STRING.name());
		imtordt.put("param12", "xitem|" + ReportParamDataType.INTEGER.name());
		imtordt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imrcvdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imrcvdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imrcvdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imrcvdt.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imrcvdt.put("param6", "xrcvtype|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param9", "xrcvnum|" + ReportParamDataType.INTEGER.name());
		imrcvdt.put("param10", "xgitem|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param11", "xcatitem|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param12", "xsubcat|" + ReportParamDataType.STRING.name());
		imrcvdt.put("param13", "xitem|" + ReportParamDataType.INTEGER.name());
		imrcvdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imissdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imissdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imissdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imissdt.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imissdt.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imissdt.put("param6", "xisstype|" + ReportParamDataType.STRING.name());
		imissdt.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		imissdt.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		imissdt.put("param9", "xissuenum|" + ReportParamDataType.INTEGER.name());
		imissdt.put("param10", "xgitem|" + ReportParamDataType.STRING.name());
		imissdt.put("param11", "xcatitem|" + ReportParamDataType.STRING.name());
		imissdt.put("param12", "xsubcat|" + ReportParamDataType.STRING.name());
		imissdt.put("param13", "xitem|" + ReportParamDataType.INTEGER.name());
		imissdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imadjdt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imadjdt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imadjdt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imadjdt.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imadjdt.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imadjdt.put("param6", "xstatus|" + ReportParamDataType.STRING.name());
		imadjdt.put("param7", "xstatusim|" + ReportParamDataType.STRING.name());
		imadjdt.put("param8", "xadjnum|" + ReportParamDataType.INTEGER.name());
		imadjdt.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		imadjdt.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		imadjdt.put("param11", "xsubcat|" + ReportParamDataType.STRING.name());
		imadjdt.put("param12", "xitem|" + ReportParamDataType.INTEGER.name());
		imadjdt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imstock.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imstock.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imstock.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imstock.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		imstock.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		imstock.put("param6", "xsubcat|" + ReportParamDataType.STRING.name());
		imstock.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		imstock.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imleddt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imleddt.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imleddt.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imleddt.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		imleddt.put("param5", "xitem|" + ReportParamDataType.INTEGER.name());
		imleddt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imledsm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imledsm.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imledsm.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imledsm.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imledsm.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imledsm.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		imledsm.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		imledsm.put("param8", "xsubcat|" + ReportParamDataType.STRING.name());
		imledsm.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		imledsm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imledsmmis.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imledsmmis.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imledsmmis.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imledsmmis.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imledsmmis.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imledsmmis.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		imledsmmis.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		imledsmmis.put("param8", "xsubcat|" + ReportParamDataType.STRING.name());
		imledsmmis.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		imledsmmis.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imagesm.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imagesm.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imagesm.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imagesm.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		imagesm.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		imagesm.put("param6", "xsubcat|" + ReportParamDataType.STRING.name());
		imagesm.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		imagesm.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imagedt.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imagedt.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imagedt.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imagedt.put("param4", "xitem|" + ReportParamDataType.INTEGER.name());
		imagedt.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imstocklow.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imstocklow.put("param2", "xgitem|" + ReportParamDataType.STRING.name());
		imstocklow.put("param3", "xcatitem|" + ReportParamDataType.STRING.name());
		imstocklow.put("param4", "xsubcat|" + ReportParamDataType.STRING.name());
		imstocklow.put("param5", "xitem|" + ReportParamDataType.INTEGER.name());
		imstocklow.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelage.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelage.put("param2", "xtype|" + ReportParamDataType.STRING.name());
		opundelage.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelage.put("param4", "xcatcus|" + ReportParamDataType.STRING.name());
		opundelage.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelage.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imundelstock.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		imundelstock.put("param2", "@xwh|" + ReportParamDataType.INTEGER.name());
		imundelstock.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opreqdtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opreqdtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opreqdtff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opreqdtff.put("param8", "xstatusreq|" + ReportParamDataType.STRING.name());
		opreqdtff.put("param9", "xdoreqnum|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		opreqdtff.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		opreqdtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opreqsmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opreqsmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opreqsmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opreqsmff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opreqsmff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opreqsmff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opreqsmff.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opreqsmff.put("param8", "xstatusreq|" + ReportParamDataType.STRING.name());
		opreqsmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		oporddtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		oporddtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		oporddtff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		oporddtff.put("param8", "xstatusord|" + ReportParamDataType.STRING.name());
		oporddtff.put("param9", "xordernum|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		oporddtff.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		oporddtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opordsmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opordsmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opordsmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opordsmff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opordsmff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opordsmff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opordsmff.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		opordsmff.put("param8", "xstatusord|" + ReportParamDataType.STRING.name());
		opordsmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdodtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdodtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdodtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdodtff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opdodtff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opdodtff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opdodtff.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		opdodtff.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		opdodtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdosmcatff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdosmcatff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdosmcatff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdosmcatff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opdosmcatff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opdosmcatff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opdosmcatff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opdosmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdosmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opdosmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opdosmff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opdosmff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opdosmff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opdosmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opcrndtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrndtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opcrndtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opcrndtff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opcrndtff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opcrndtff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opcrndtff.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		opcrndtff.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		opcrndtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opcrnsmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrnsmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opcrnsmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opcrnsmff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		opcrnsmff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opcrnsmff.put("param6", "xwh|" + ReportParamDataType.INTEGER.name());
		opcrnsmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundeldtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundeldtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundeldtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundeldtff.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundeldtff.put("param5", "xarea|" + ReportParamDataType.INTEGER.name());
		opundeldtff.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opundeldtff.put("param7", "xwh|" + ReportParamDataType.INTEGER.name());
		opundeldtff.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundeldtff.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundeldtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelsmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelsmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundelsmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundelsmff.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundelsmff.put("param5", "xarea|" + ReportParamDataType.INTEGER.name());
		opundelsmff.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelsmff.put("param7", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelsmff.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundelsmff.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundelsmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelcusff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelcusff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		opundelcusff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		opundelcusff.put("param4", "xtype|" + ReportParamDataType.STRING.name());
		opundelcusff.put("param5", "xarea|" + ReportParamDataType.INTEGER.name());
		opundelcusff.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelcusff.put("param7", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelcusff.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		opundelcusff.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		opundelcusff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		armrdtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		armrdtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		armrdtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		armrdtff.put("param4", "xbank|" + ReportParamDataType.INTEGER.name());
		armrdtff.put("param5", "xarea|" + ReportParamDataType.INTEGER.name());
		armrdtff.put("param6", "xcus|" + ReportParamDataType.INTEGER.name());
		armrdtff.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		armrdtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		aradjdtff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		aradjdtff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		aradjdtff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		aradjdtff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		aradjdtff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		aradjdtff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		arledsmff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		arledsmff.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		arledsmff.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		arledsmff.put("param4", "xarea|" + ReportParamDataType.INTEGER.name());
		arledsmff.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		arledsmff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		caitemff.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		caitemff.put("param2", "xorg|" + ReportParamDataType.INTEGER.name());
		caitemff.put("param3", "xcatitem|" + ReportParamDataType.STRING.name());
		caitemff.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		caitemffrp12.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		caitemffrp12.put("param2", "xorg|" + ReportParamDataType.INTEGER.name());
		caitemffrp12.put("param3", "xcatitem|" + ReportParamDataType.STRING.name());
		caitemffrp12.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imtordtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtordtrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imtordtrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imtordtrp17.put("param4", "xfwh|" + ReportParamDataType.INTEGER.name());
		imtordtrp17.put("param5", "xtwh|" + ReportParamDataType.INTEGER.name());
		imtordtrp17.put("param6", "xstatus|" + ReportParamDataType.STRING.name());
		imtordtrp17.put("param7", "xstatusim|" + ReportParamDataType.STRING.name());
		imtordtrp17.put("param8", "xtornum|" + ReportParamDataType.INTEGER.name());
		imtordtrp17.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		imtordtrp17.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		imtordtrp17.put("param11", "xsubcat|" + ReportParamDataType.STRING.name());
		imtordtrp17.put("param12", "xitem|" + ReportParamDataType.INTEGER.name());
		imtordtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imrcvdtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imrcvdtrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imrcvdtrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imrcvdtrp17.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imrcvdtrp17.put("param6", "xrcvtype|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param9", "xrcvnum|" + ReportParamDataType.INTEGER.name());
		imrcvdtrp17.put("param10", "xgitem|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param11", "xcatitem|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param12", "xsubcat|" + ReportParamDataType.STRING.name());
		imrcvdtrp17.put("param13", "xitem|" + ReportParamDataType.INTEGER.name());
		imrcvdtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imissdtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imissdtrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imissdtrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imissdtrp17.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imissdtrp17.put("param6", "xisstype|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param7", "xstatus|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param8", "xstatusim|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param9", "xissuenum|" + ReportParamDataType.INTEGER.name());
		imissdtrp17.put("param10", "xgitem|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param11", "xcatitem|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param12", "xsubcat|" + ReportParamDataType.STRING.name());
		imissdtrp17.put("param13", "xitem|" + ReportParamDataType.INTEGER.name());
		imissdtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imadjdtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imadjdtrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imadjdtrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imadjdtrp17.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imadjdtrp17.put("param6", "xstatus|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param7", "xstatusim|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param8", "xadjnum|" + ReportParamDataType.INTEGER.name());
		imadjdtrp17.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param11", "xsubcat|" + ReportParamDataType.STRING.name());
		imadjdtrp17.put("param12", "xitem|" + ReportParamDataType.INTEGER.name());
		imadjdtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imstockrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imstockrp17.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imstockrp17.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imstockrp17.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		imstockrp17.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		imstockrp17.put("param6", "xsubcat|" + ReportParamDataType.STRING.name());
		imstockrp17.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		imstockrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imleddtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imleddtrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imleddtrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imleddtrp17.put("param4", "xwh|" + ReportParamDataType.INTEGER.name());
		imleddtrp17.put("param5", "xitem|" + ReportParamDataType.INTEGER.name());
		imleddtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imledsmrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imledsmrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imledsmrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imledsmrp17.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imledsmrp17.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imledsmrp17.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		imledsmrp17.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		imledsmrp17.put("param8", "xsubcat|" + ReportParamDataType.STRING.name());
		imledsmrp17.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		imledsmrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imledsmmisrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imledsmmisrp17.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		imledsmmisrp17.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		imledsmmisrp17.put("param4", "xwhcat|" + ReportParamDataType.STRING.name());
		imledsmmisrp17.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		imledsmmisrp17.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		imledsmmisrp17.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		imledsmmisrp17.put("param8", "xsubcat|" + ReportParamDataType.STRING.name());
		imledsmmisrp17.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		imledsmmisrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imagesmrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imagesmrp17.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imagesmrp17.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imagesmrp17.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		imagesmrp17.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		imagesmrp17.put("param6", "xsubcat|" + ReportParamDataType.STRING.name());
		imagesmrp17.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		imagesmrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imagedtrp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imagedtrp17.put("param2", "xwhcat|" + ReportParamDataType.STRING.name());
		imagedtrp17.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		imagedtrp17.put("param4", "xitem|" + ReportParamDataType.INTEGER.name());
		imagedtrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		opundelagerp17.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opundelagerp17.put("param2", "xtype|" + ReportParamDataType.STRING.name());
		opundelagerp17.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		opundelagerp17.put("param4", "xcatcus|" + ReportParamDataType.STRING.name());
		opundelagerp17.put("param5", "xcus|" + ReportParamDataType.INTEGER.name());
		opundelagerp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		imundelstockrp17.put("param1", "@zid|" + ReportParamDataType.INTEGER.name());
		imundelstockrp17.put("param2", "@xwh|" + ReportParamDataType.INTEGER.name());
		imundelstockrp17.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
	}
}
