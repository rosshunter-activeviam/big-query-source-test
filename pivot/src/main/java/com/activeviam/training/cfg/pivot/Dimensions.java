package com.activeviam.training.cfg.pivot;

import static com.activeviam.training.constants.StoreAndFieldConstants.*;
import com.activeviam.desc.build.ICanBuildCubeDescription;
import com.activeviam.desc.build.dimensions.ICanStartBuildingDimensions;
import com.activeviam.training.constants.BucketValues;
import com.quartetfs.biz.pivot.definitions.IActivePivotInstanceDescription;

public class Dimensions {
	
	/**
	 * Adds the dimensions descriptions to the input
	 * builder.
	 *
	 * @param builder The cube builder
	 * @return The builder for chained calls
	 */
	public static ICanBuildCubeDescription<IActivePivotInstanceDescription> build (ICanStartBuildingDimensions builder) {
		
		return builder
				// Risk Dimensions
				.withSingleLevelDimensions(
						RISK_BUCKET__RISKTYPE,
						RISK_BUCKET__MARKETDATAID,
						RISK_BUCKET__SCENARIOID,
						RISK_BUCKET__VALUATIONMODEL,
						RISK_BUCKET__RUN,
						RISK_BUCKET__ERRORSTATUS,
						
						// Remove this for the dictionary exercise
						RISK_BUCKET__RISKCURRENCY
						)
				
				// Market Data Dimensons
				.withSingleLevelDimensions(
						MD_BUCKET__INSTRUMENTID,
						MD_BUCKET__MDTYPE,
						MD_BUCKET__VALUATIONMODEL,
						MD_BUCKET__CURVENAME				
						)
				.withSingleLevelDimension(MD_BUCKET__MDBUCKET).withFirstObjectsFromList(BucketValues.getBuckets())
				.withSingleLevelDimension(MD_BUCKET__MDSUMMARYBUCKET).withFirstObjectsFromList(BucketValues.getSummaryBuckets())
				.withSingleLevelDimension(MD_BUCKET__MDSUMMARYGROUP).withFirstObjectsFromList(BucketValues.getBuckets())
						
				// Trade Dimensions
				.withSingleLevelDimensions(
						TRADES__BOOKINGSYSTEM,
						TRADES__INSTRUMENTID,
						TRADES__TRADECURRENCY
				)
				
				
				.withDimension("Status")
					.withHierarchy("Lifecycle Status").withLevel(TRADES__LIFECYCLESTATUS)
					.withHierarchy("Cleared Status").withLevel(TRADES__CLEAREDTRADESTATUS)
					.withHierarchy("Settled Status").withLevel(TRADES__SETTLEDTRADESTATUS)
					
				
				.withSingleLevelDimensions(
						TRADES__STATICTRADERID,
						TRADES__STATICMOID,
						TRADES__STATICSALESPERSONID,
						TRADES__STATICBOOKID,
						TRADES__STATICCOUNTERPARTYID
				)						
										
				.withDimension("Trades").withHierarchyOfSameName().withLevels(RISK_BUCKET__TRADEID)

				;
		
	}

}
