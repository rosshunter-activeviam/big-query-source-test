package com.activeviam.training.cfg.pivot.vectorised;

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
						
				// Trade Dimensions
				.withSingleLevelDimensions(
						TRADES__BOOKINGSYSTEM,
						TRADES__INSTRUMENTID,
						TRADES__TRADECURRENCY
				)

				.withDimension(MD_BUCKET__MDBUCKET)
				.withHierarchyOfSameName()
				.factless()
				.withStoreName(BUCKETS_STORE_NAME)
					.withLevel(MD_BUCKET__MDBUCKET)
					.withFieldName(MD_BUCKET__MDBUCKET)
					.withFirstObjects(BucketValues.getBuckets())
					
				.withDimension(MD_BUCKET__MDSUMMARYBUCKET)
				.withHierarchyOfSameName()
				.factless()
				.withStoreName(BUCKETS_STORE_NAME)
					.withLevel(MD_BUCKET__MDSUMMARYBUCKET)
					.withFieldName(MD_BUCKET__MDSUMMARYBUCKET)
					.withFirstObjects(BucketValues.getBuckets())
					
				.withDimension(MD_BUCKET__MDSUMMARYGROUP)
				.withHierarchyOfSameName()
				.factless()
				.withStoreName(BUCKETS_STORE_NAME)
					.withLevel(MD_BUCKET__MDSUMMARYGROUP)
					.withFieldName(MD_BUCKET__MDSUMMARYGROUP)
					.withFirstObjects(BucketValues.getSummaryBuckets())
				
				
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
