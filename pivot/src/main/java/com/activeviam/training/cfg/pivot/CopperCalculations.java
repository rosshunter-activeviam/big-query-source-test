package com.activeviam.training.cfg.pivot;

import static com.activeviam.copper.columns.Columns.col;
import static com.activeviam.copper.columns.Columns.max;
import static com.activeviam.copper.columns.Columns.sum;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET_INTRADAY_STORE_NAME;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__ID;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__MDBUCKET;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__VALUE_INTRADAY;
import static com.activeviam.training.constants.StoreAndFieldConstants.MD_BUCKET__VALUE_SOD;
import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET__MARKETDATAID;
import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET__VALUE;
import static com.activeviam.training.constants.StoreAndFieldConstants.TRADES__NOTIONAL;

import com.activeviam.copper.builders.BuildingContext;
import com.activeviam.copper.builders.dataset.Datasets.Dataset;
import com.activeviam.copper.builders.dataset.Datasets.StoreDataset;
import com.activeviam.copper.columns.Columns;

public class CopperCalculations {
	
	/* ************************* */
	/* Measures names */
	/* ************************* */
	public static final String RISK_VALUE_SUM = "Delta";
	public static final String NOTIONAL_SUM = "Trade Notional";
	public static final String MD_VALUE_SOD = "SoD Value";
	public static final String MD_VALUE_INTRADAY = "Intraday Value";
	public static final String RATE_CHANGE = "Rate Change";
	public static final String RSBPL = "RSBPL";
	
	
	/* ********** */
	/* Folders */
	/* ********** */
	
	public static final String MARKET_DATA = "Market Data";
	
	/* ********** */
	/* Formatters */
	/* ********** */
	
	/**
	 * The formatter for double measures with at most 2 digits after the decimal separator.
	 */
	public static final String DOUBLE_FORMATTER = "DOUBLE[#,###.00;-#,###.00]";
	
	/**
	 * The formatter for double measures with at most 4 digits after the decimal separator.
	 */
	public static final String DOUBLE_FORMATTER_LOTS_VALUES = "DOUBLE[#,###.0000;-#,###.0000]";
	
	/**
	 * The int formatter.
	 */
	public static final String INT_FORMATTER = "INT[#,###]";
	
	/**
	 * The CoPPer calculations to add to the cube
	 * @param context The context with which to build the calculations.
	 */
	public static void buildCalculations(BuildingContext context) {
		someAggregatedMeasures(context).publish();	
		
		marketDataMeasures(context).agg(
				sum(RSBPL).as(RSBPL)
				).publish();
		
		marketDataMeasures(context)
			.select(MD_VALUE_INTRADAY, MD_VALUE_SOD, RATE_CHANGE)
			.doNotAggregateAbove().publish();
	}
	
	/**
	 * Creates aggregated measures.
	 *
	 * @param context The CoPPer build context.
	 *
	 * @return The Dataset of the aggregated measures.
	 */		
	protected static Dataset someAggregatedMeasures(final BuildingContext context) {		
		return context
				.withFormatter(DOUBLE_FORMATTER)
				.createDatasetFromFacts()
				.agg(
						sum(RISK_BUCKET__VALUE).as("Delta.SUM"),
						sum(TRADES__NOTIONAL).as(NOTIONAL_SUM)
					);		
	}
	
	protected static Dataset marketDataMeasures(final BuildingContext context) {
		StoreDataset mdIntraday = context.createDatasetFromStore(MD_BUCKET_INTRADAY_STORE_NAME);		
		return context
				.withinFolder(MARKET_DATA)
				.createDatasetFromFacts()
				.join(mdIntraday,Columns.mapping(RISK_BUCKET__MARKETDATAID).to(MD_BUCKET__ID))
				.groupBy(MD_BUCKET__MDBUCKET)				
				.agg(
						max(MD_BUCKET__VALUE_INTRADAY).as(MD_VALUE_INTRADAY),
						max(MD_BUCKET__VALUE_SOD).as(MD_VALUE_SOD),
						sum(RISK_BUCKET__VALUE).as(RISK_VALUE_SUM + "LEAF")
					)
				.withColumn(RATE_CHANGE, col(MD_VALUE_SOD).minus(col(MD_VALUE_INTRADAY)))
				.withColumn(RSBPL, col(RATE_CHANGE).multiply(col(RISK_VALUE_SUM + "LEAF")))
				;
	}


}
