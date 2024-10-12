package com.zayaanit.enums;

import java.util.Map;

import com.zayaanit.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	
	
	armr("FA31", "Money Receipt", "armr.rpt", ReportParamMap.armr, "Y", false),
	arledd("FA35", "Customer Ledger", "arledd.rpt", ReportParamMap.arledd, "Y", false),
	opundel("FA35", "Undelivered Items", "opundel.rpt", ReportParamMap.opundel, "Y", false),
	imtor("IM11", "Inventory Transfer", "imtor.rpt", ReportParamMap.imtor, "Y", false),
	imtorprev("IM11", "Inventory Transfer", "imtorprev.rpt", ReportParamMap.imtor, "Y", false),
	imtorreq("IM11", "Store Requisition", "imtorreq.rpt", ReportParamMap.imtorreq, "Y", false),
	imrcv("IM12", "Inventory Receive", "imrcv.rpt", ReportParamMap.imrcv, "Y", false),
	imiss("IM13", "Inventory Issue", "imiss.rpt", ReportParamMap.imiss, "Y", false),
	imadj("IM14", "Inventory Adjustment", "imadj.rpt", ReportParamMap.imadj, "Y", false),
	opreq("SO12", "Sales Requisition", "opreq.rpt", ReportParamMap.opreq, "Y", false),
	opreq2("SO12", "Sales Requisition", "opreq2.rpt", ReportParamMap.opreq, "Y", false),
	opreqtoord("SO13", "Requisition To order", "opreqtoord.rpt", ReportParamMap.opreqtoord, "Y", false),
	opord("SO14", "Direct Sales Order", "opord.rpt", ReportParamMap.opord, "Y", false),
	opdoinv("SO14", "Direct Invoice", "opdoinv.rpt", ReportParamMap.opdoinv, "Y", false),
	opdoinvdv("SO14", "Direct Invoice", "opdoinvdv.rpt", ReportParamMap.opdoinvdv, "Y", false),
	opdoinvsv("SO14", "Direct Invoice", "opdoinvsv.rpt", ReportParamMap.opdoinvsv, "Y", false),
	opordinv("SO15", "Print Invoices", "opordinv.rpt", ReportParamMap.opordinv, "Y", false),
	opordprev("SO16", "Sales Order to Invoice", "opordprev.rpt", ReportParamMap.opordprev, "Y", false),
	opdoinvd("SO17", "Invoice Detail", "opdoinvd.rpt", ReportParamMap.opdoinvd, "Y", false),
	opdoclnd("SO17", "Challan", "opdoclnd.rpt", ReportParamMap.opdoclnd, "Y", false),
	doprev("SO17", "Invoice Preview", "doprev.rpt", ReportParamMap.doprev, "Y", false),
	doprevd("SO17", "Invoice Preview", "doprevd.rpt", ReportParamMap.doprev, "Y", false),
	opdocln("SO18", "Challan", "opdocln.rpt", ReportParamMap.opdocln, "Y", false),
	opcrn("SO19", "Direct Return", "opcrn.rpt", ReportParamMap.opcrn, "Y", false),
	opdolist("SO21", "List of invoices", "opdolist.rpt", ReportParamMap.opdolist, "Y", false),
	opshipd("SP11", "Shipment Detail", "opshipd.rpt", ReportParamMap.opshipd, "Y", false),
	opships("SP11", "Shipment Summary", "opships.rpt", ReportParamMap.opships, "Y", false),
	opshipg("SP11", "Gate Pass", "opshipg.rpt", ReportParamMap.opshipg, "Y", false),

	// REPORT MODULES REPORT
	RP01("RP01", "User Listing Report", "RP01.rpt", ReportParamMap.RP01, "Y", false),
	RP02("RP02", "Profile Wise Access Report", "RP02.rpt", ReportParamMap.RP02, "Y", false),
	RP03("RP03", "Chart of Account Detail", "RP03.rpt", ReportParamMap.RP03, "Y", false),
	RP04("RP04", "Chart of Account Summary", "RP04.rpt", ReportParamMap.RP04, "Y", false),
	RP05("RP05", "Sub Account Report", "RP05.rpt", ReportParamMap.RP05, "Y", false),
	RP06("RP06", "General Journal", "RP06.rpt", ReportParamMap.RP06, "Y", false),
	RP07("RP07", "Account Ledger", "RP07.rpt", ReportParamMap.RP07, "Y", false),
	RP08("RP08", "", "RP08.rpt", ReportParamMap.RP08, "Y", false),
	RP09("RP09", "", "RP09.rpt", ReportParamMap.RP09, "Y", false),
	RP10("RP10", "", "RP10.rpt", ReportParamMap.RP10, "Y", false),
	RP11("RP11", "", "RP11.rpt", ReportParamMap.RP11, "Y", false),
	RP12("RP12", "", "RP12.rpt", ReportParamMap.RP12, "Y", false),
	RP13("RP13", "", "RP13.rpt", ReportParamMap.RP13, "Y", false),
	RP14("RP14", "", "RP14.rpt", ReportParamMap.RP14, "Y", false),
	RP15("RP15", "", "RP15.rpt", ReportParamMap.RP15, "Y", false),
	RP16("RP16", "", "RP16.rpt", ReportParamMap.RP16, "Y", false),
	RP17("RP16", "", "RP17.rpt", ReportParamMap.RP17, "Y", false),
	
	
	// RP11
	opdodtmis("RP11", "Sales Invoice Detail (MIS)", "opdodtmis.rpt", ReportParamMap.opdodtmis, "Y", false),
	opdosmmis("RP11", "Sales Invoice Summary (MIS)", "opdosmmis.rpt", ReportParamMap.opdosmmis, "Y", false),
	imstockmis("RP11", "Current Stock (MIS)", "imstockmis.rpt", ReportParamMap.imstockmis, "Y", false),
	imleddtmis("RP11", "Item Ledger Detail (MIS)", "imleddtmis.rpt", ReportParamMap.imleddtmis, "Y", false),
	imledsmmis("RP11", "Date Wise Stock Status (MIS)", "imledsmmis.rpt", ReportParamMap.imledsmmis, "Y", false),
	arledsmtrmis("RP11", "Receivable & Collection Statement", "arledsmtr.rpt", ReportParamMap.arledsmtrmis, "Y", false),
	cacusarmis("RP11", "Customer Master Report (MIS)", "cacusarmis.rpt", ReportParamMap.cacusarmis, "Y", false),
	opshipsdt("RP11", "Shipment Listing Detail", "opshipsdt.rpt", ReportParamMap.opshipsdt, "Y", false),
	opshipssm("RP11", "Shipment Listing Summary", "opshipssm.rpt", ReportParamMap.opshipssm, "Y", false),

	// RP12
	xusers("RP12", "User Listing Report", "xusers.rpt", ReportParamMap.xusers, "Y", false),
	pdmst("RP12", "Employee Master Report", "pdmst.rpt", ReportParamMap.pdmst, "Y", false),
	caitem("RP12", "Item Master Report", "caitem.rpt", ReportParamMap.caitem, "Y", false),
	cacusar("RP12", "Customer Master Report", "cacusar.rpt", ReportParamMap.cacusar, "Y", false),
	cabank("RP12", "Bank Master Report", "cabank.rpt", ReportParamMap.cabank, "Y", false),
	opvhls("RP12", "Vehile Master Report", "opvhls.rpt", ReportParamMap.opvhls, "Y", false),
	caitemffrp12("RP12", "Item VS Organization Report", "caitemff.rpt", ReportParamMap.caitemffrp12, "Y", false),

	// RP13
	arleddt("RP13", "Customer Ledger Detail", "arleddt.rpt", ReportParamMap.arleddt, "Y", false),
	arledsm("RP13", "Customer Ledger Summary", "arledsm.rpt", ReportParamMap.arledsm, "Y", false),
	armrdt("RP13", "Money Receipt Listing", "armrdt.rpt", ReportParamMap.armrdt, "Y", false),
	armrbnkdt("RP13", "Bank Receive Listing", "armrbnkdt.rpt", ReportParamMap.armrbnkdt, "Y", false),
	aradjdt("RP13", "Customer Adjustment Listing", "aradjdt.rpt", ReportParamMap.aradjdt, "Y", false),
	arcollec("RP13", "Collection Listing Report", "arcollec.rpt", ReportParamMap.arcollec, "Y", false),
	arledsmtr("RP13", "Receivable & Collection Statement", "arledsmtr.rpt", ReportParamMap.arledsmtr, "Y", false),

	// RP14
	opreqdt("RP14", "Sales Requisition Detail", "opreqdt.rpt", ReportParamMap.opreqdt, "Y", false),
	opreqsm("RP14", "Sales Requisition Summary", "opreqsm.rpt", ReportParamMap.opreqsm, "Y", false),
	oporddt("RP14", "Sales Order Detail", "oporddt.rpt", ReportParamMap.oporddt, "Y", false),
	opordsm("RP14", "Sales Order Summary", "opordsm.rpt", ReportParamMap.opordsm, "Y", false),
	opdodt("RP14", "Sales Invoice Detail", "opdodt.rpt", ReportParamMap.opdodt, "Y", false),
	opdosmcat("RP14", "Sales Invoice Item Summary", "opdosmcat.rpt", ReportParamMap.opdosmcat, "Y", false),
	opdosm("RP14", "Sales Invoice Summary", "opdosm.rpt", ReportParamMap.opdosm, "Y", false),
	opcrndt("RP14", "Sales Return Detail", "opcrndt.rpt", ReportParamMap.opcrndt, "Y", false),
	opcrnsm("RP14", "Sales Return Summary", "opcrnsm.rpt", ReportParamMap.opcrnsm, "Y", false),
	opundeldt("RP14", "Undelivered Item Details", "opundeldt.rpt", ReportParamMap.opundeldt, "Y", false),
	opundelsm("RP14", "Undelivered Item Summary", "opundelsm.rpt", ReportParamMap.opundelsm, "Y", false),
	opundelsmtr("RP14", "Undelivered Item Summary (Hierarchy)", "opundelsmtr.rpt", ReportParamMap.opundelsmtr, "Y", false),
	opundelcus("RP14", "Party Wise Undelivered Statement", "opundelcus.rpt", ReportParamMap.opundelcus, "Y", false),
	opdodtffRP14("RP14", "Sales Invoice Detail (Area)", "opdodtff.rpt", ReportParamMap.opdodtff, "Y", false),
	opdosmcatffRP14("RP14", "Sales Invoice Item Summary (Area)", "opdosmcatff.rpt", ReportParamMap.opdosmcatff, "Y", false),
	opdosmffRP14("RP14", "Sales Invoice Summary (Area)", "opdosmff.rpt", ReportParamMap.opdosmff, "Y", false),

	// RP15
	imtordt("RP15", "Inventory Transfer Listing", "imtordt.rpt", ReportParamMap.imtordt, "Y", false),
	imrcvdt("RP15", "Inventory Receive Listing", "imrcvdt.rpt", ReportParamMap.imrcvdt, "Y", false),
	imissdt("RP15", "Inventory Issue Listing", "imissdt.rpt", ReportParamMap.imissdt, "Y", false),
	imadjdt("RP15", "Inventory Adjustment Listing", "imadjdt.rpt", ReportParamMap.imadjdt, "Y", false),
	imstock("RP15", "Current Stock", "imstock.rpt", ReportParamMap.imstock, "Y", false),
	imleddt("RP15", "Item Ledger Detail", "imleddt.rpt", ReportParamMap.imleddt, "Y", false),
	imledsm("RP15", "Date Wise Stock Status", "imledsm.rpt", ReportParamMap.imledsm, "Y", false),
	imstocklow("RP15", "Inventory Low Status", "imstocklow.rpt", ReportParamMap.imstocklow, "Y", false),
	opundelage("RP15", "Undelivered Ageing Report", "opundelage.rpt", ReportParamMap.opundelage, "Y", false),
	imundelstock("RP15", "Undelivered VS Current Stock", "imundelstock.rpt", ReportParamMap.imundelstock, "Y", false),
	imagedt("RP15", "Inventory Ageing Detail", "imagedt.rpt", ReportParamMap.imagedt, "Y", false),
	imagesm("RP15", "Inventory Ageing Summary", "imagesm.rpt", ReportParamMap.imagesm, "Y", false),

	// RP16
	opreqdtff("RP16", "Sales Requisition Detail", "opreqdtff.rpt", ReportParamMap.opreqdtff, "Y", false),
	opreqsmff("RP16", "Sales Requisition Summary", "opreqsmff.rpt", ReportParamMap.opreqsmff, "Y", false),
	oporddtff("RP16", "Sales Order Detail", "oporddtff.rpt", ReportParamMap.oporddtff, "Y", false),
	opordsmff("RP16", "Sales Order Summary", "opordsmff.rpt", ReportParamMap.opordsmff, "Y", false),
	opdodtff("RP16", "Sales Invoice Detail", "opdodtff.rpt", ReportParamMap.opdodtff, "Y", false),
	opdosmcatff("RP16", "Sales Invoice Item Summary", "opdosmcatff.rpt", ReportParamMap.opdosmcatff, "Y", false),
	opdosmff("RP16", "Sales Invoice Summary", "opdosmff.rpt", ReportParamMap.opdosmff, "Y", false),
	//opcrndtff("RP16", "Sales Return Detail", "opcrndtff.rpt", ReportParamMap.opcrndtff, "Y", false),
	opcrnsmff("RP16", "Sales Return Summary", "opcrnsmff.rpt", ReportParamMap.opcrnsmff, "Y", false),
	opundeldtff("RP16", "Undelivered Item Details", "opundeldtff.rpt", ReportParamMap.opundeldtff, "Y", false),
	opundelsmff("RP16", "Undelivered Item Summary", "opundelsmff.rpt", ReportParamMap.opundelsmff, "Y", false),
	opundelcusff("RP16", "Party Wise Undelivered Statement", "opundelcusff.rpt", ReportParamMap.opundelcusff, "Y", false),
	arleddt16("RP16", "Customer Ledger Detail", "arleddt.rpt", ReportParamMap.arleddt, "Y", false),
	armrdtff("RP16", "Money Receipt Listing", "armrdtff.rpt", ReportParamMap.armrdtff, "Y", false),
	aradjdtff("RP16", "Customer Adjustment Listing", "aradjdtff.rpt", ReportParamMap.aradjdtff, "Y", false),
	arledsmff("RP16", "Receivable & Collection Statement", "arledsmff.rpt", ReportParamMap.arledsmff, "Y", false),
	caitemff("RP16", "Item Master Report (Sales)", "caitemff.rpt", ReportParamMap.caitemff, "Y", false),

	// RP17
	imtordtrp17("RP17", "Inventory Transfer Listing", "imtordt.rpt", ReportParamMap.imtordtrp17, "Y", false),
	imrcvdtrp17("RP17", "Inventory Receive Listing", "imrcvdt.rpt", ReportParamMap.imrcvdtrp17, "Y", false),
	imissdtrp17("RP17", "Inventory Issue Listing", "imissdt.rpt", ReportParamMap.imissdtrp17, "Y", false),
	imadjdtrp17("RP17", "Inventory Adjustment Listing", "imadjdt.rpt", ReportParamMap.imadjdtrp17, "Y", false),
	imstockrp17("RP17", "Current Stock", "imstock.rpt", ReportParamMap.imstockrp17, "Y", false),
	imleddtrp17("RP17", "Item Ledger Detail", "imleddt.rpt", ReportParamMap.imleddtrp17, "Y", false),
	imledsmrp17("RP17", "Date Wise Stock Status", "imledsm.rpt", ReportParamMap.imledsmrp17, "Y", false),
	opundelagerp17("RP17", "Undelivered Ageing Report", "opundelage.rpt", ReportParamMap.opundelagerp17, "Y", false),
	imagedtrp17("RP17", "Inventory Ageing Detail", "imagedt.rpt", ReportParamMap.imagedtrp17, "Y", false),
	imagesmrp17("RP17", "Inventory Ageing Summary", "imagesm.rpt", ReportParamMap.imagesmrp17, "Y", false),
	imundelstockrp17("RP17", "Undelivered VS Current Stock", "imundelstock.rpt", ReportParamMap.imundelstockrp17, "Y", false),
	opshipsdtrp17("RP17", "Shipment Listing Detail", "opshipsdt.rpt", ReportParamMap.opshipsdt, "Y", false),
	opshipssmrp17("RP17", "Shipment Listing Summary", "opshipssm.rpt", ReportParamMap.opshipssm, "Y", false);

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
