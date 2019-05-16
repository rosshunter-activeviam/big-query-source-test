/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.constants;

public class StoreAndFieldConstants {
	
	/*********************** Stores names **********************/
	public static final String TRADES_STORE_NAME = "Trades";
	public static final String STATIC_BOOK_STORE_NAME = "Static Book";
	public static final String RISK_BUCKET_STORE_NAME = "Risk Bucket";
	public static final String MD_BUCKET_SOD_STORE_NAME = "MD Bucket SoD";
	public static final String MD_BUCKET_INTRADAY_STORE_NAME = "MD Bucket Intraday";
	public static final String FX_STORE_NAME = "FX";
	public static final String BUCKETS_STORE_NAME = "Buckets";
	
	
	/********************* Stores fields ***********************/
	public static final String TRADES__SEQID = "Seq Id";
	public static final String TRADES__BOOKINGSYSTEM = "Booking System";
	public static final String TRADES__INSTRUMENTID = "InstrumentId";
	public static final String TRADES__TRADECURRENCY = "Trade Currency";
	public static final String TRADES__TRADEEVENT = "tradeEvent";
	public static final String TRADES__LIFECYCLESTATUS = "Lifecycle Status";
	public static final String TRADES__CLEAREDTRADESTATUS = "Cleared Trade Status";
	public static final String TRADES__SETTLEDTRADESTATUS = "Settled Trade Status";
	public static final String TRADES__NOTIONAL = "Notional";
	public static final String TRADES__STATICTRADERID = "TraderId";
	public static final String TRADES__STATICMOID = "MoId";
	public static final String TRADES__STATICSALESPERSONID = "SalespersonId";
	public static final String TRADES__STATICCOUNTERPARTYID = "CounterpartyId";
	public static final String TRADES__STATICBOOKID = "BookId";
	
	
	// Static Book
	public static final String STATIC_BOOK__ID = "id";
	public static final String STATIC_BOOK__BOOKNAME = "Book Name";
	public static final String STATIC_BOOK__BOOKINGSYSTEM = "Booking System";
	public static final String STATIC_BOOK__REGION = "region";
	public static final String STATIC_BOOK__CUTTIME = "cutTime";
	public static final String STATIC_BOOK__STATUS = "status";
	public static final String STATIC_BOOK__BOOKTYPE = "bookType";
	public static final String STATIC_BOOK__INTERNALRISKCONTROLFRAMEWORK = "internalRiskControlFramework";
	public static final String STATIC_BOOK__FXTREATMENT = "fxTreatment";
	public static final String STATIC_BOOK__RISKRELEVENT = "riskRelevent";
	public static final String STATIC_BOOK__PNLRELEVENT = "pnlRelevent";
	public static final String STATIC_BOOK__DESCRIPTION = "book description";
	public static final String STATIC_BOOK__BOOKOWNERID = "bookOwnerId";
	public static final String STATIC_BOOK__COSTCENTER = "costCenter";
	public static final String STATIC_BOOK__COSTCENTERTYPE = "costCenterType";
	public static final String STATIC_BOOK__SUBDESKID = "subDeskId";
	public static final String STATIC_BOOK__SUBDESK = "subdesk";
	public static final String STATIC_BOOK__SUBDESKTYPE = "subDeskType";
	public static final String STATIC_BOOK__DESKID = "DeskId";
	public static final String STATIC_BOOK__DESKCODE = "Desk Code";
	public static final String STATIC_BOOK__DESK = "Desk";
	public static final String STATIC_BOOK__DESKREGION = "Desk Region";
	public static final String STATIC_BOOK__DESKCITY = "Desk City";
	public static final String STATIC_BOOK__FUNCTIONID = "FunctionId";
	public static final String STATIC_BOOK__FUNCTION = "Function";
	public static final String STATIC_BOOK__FUNCTIONCAT = "FunctionCat";
	public static final String STATIC_BOOK__SEGMENTID = "SegmentId";
	public static final String STATIC_BOOK__SEGMENT = "Segment";
	public static final String STATIC_BOOK__SECTORID = "SectorId";
	public static final String STATIC_BOOK__SECTOR = "Sector";
	public static final String STATIC_BOOK__AREAID = "AreaId";
	public static final String STATIC_BOOK__AREA = "Area";
	public static final String STATIC_BOOK__GROUPID = "GroupId";
	public static final String STATIC_BOOK__GROUP = "Group";
	public static final String STATIC_BOOK__ENDDATE = "endDate";
	public static final String STATIC_BOOK__BOOKREMITDATE = "bookRemitDate";
	
	// Risk Buckets
	public static final String RISK_BUCKET__ID = "id";
	public static final String RISK_BUCKET__TRADEID = "Trade Id";
	public static final String RISK_BUCKET__RISKTYPE = "Risk Type";
	public static final String RISK_BUCKET__RISKCURRENCY = "Risk Currency";
	public static final String RISK_BUCKET__VALUE = "Value";
	public static final String RISK_BUCKET__MARKETDATAID = "MarketDataId";
	public static final String RISK_BUCKET__SCENARIOID = "ScenarioId";
	public static final String RISK_BUCKET__VALUATIONMODEL = "Valuation Model";
	public static final String RISK_BUCKET__RUN = "Risk Run";
	public static final String RISK_BUCKET__ERRORSTATUS = "Risk Error Status";
	
	// MD Buckets
	public static final String MD_BUCKET__ID = "id";
	public static final String MD_BUCKET__INSTRUMENTID = "mdInstrumentId";
	public static final String MD_BUCKET__MDTYPE = "MD Type";
	public static final String MD_BUCKET__MDBUCKET = "MD Bucket";
	public static final String MD_BUCKET__VALUE_SOD = "MD Value SoD";
	public static final String MD_BUCKET__VALUE_INTRADAY = "MD Value Intraday";
	public static final String MD_BUCKET__VALUATIONMODEL = "MD Valuation Model";
	public static final String MD_BUCKET__RUN = "MD Run";
	public static final String MD_BUCKET__VERSION_SOD = "mdVersionSoD";
	public static final String MD_BUCKET__VERSION_INTRADAY = "mdVersionIntraday";
	public static final String MD_BUCKET__ERRORSTATUS = "mdErrorStatus";
	
	public static final String MD_BUCKET__CURVENAME = "Curve Name";
	public static final String MD_BUCKET__MDSUMMARYGROUP = "Summary Group";
	public static final String MD_BUCKET__MDSUMMARYBUCKET = "Summary Bucket";
	public static final String MD_BUCKET__MARKETDATAVECTORID = "marketDataVectorId";
	
	// FX Store
	public static final String FX__BASE_CURRENCY = "Base Currency";
	public static final String FX__TARGET_CURRENCY = "Target Currency";
	public static final String FX__RATE = "Rate";
	

}
