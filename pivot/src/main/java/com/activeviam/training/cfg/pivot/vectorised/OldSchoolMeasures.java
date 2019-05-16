package com.activeviam.training.cfg.pivot.vectorised;

import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET__VALUE;
import static com.activeviam.training.constants.StoreAndFieldConstants.RISK_BUCKET__RISKCURRENCY;
import com.activeviam.copper.LevelCoordinate;
import com.activeviam.desc.build.ICanStartBuildingMeasures;
import com.activeviam.desc.build.IHasAtLeastOneMeasure;
import com.activeviam.training.postprocessor.impl.AFxPostprocessor;
import com.activeviam.training.postprocessor.impl.DeltaSumVector;
import com.activeviam.training.postprocessor.impl.FxWithCachePostProcessor;
import com.activeviam.training.postprocessor.impl.FxWithoutCachePostProcessor;
import com.quartetfs.biz.pivot.postprocessing.IPostProcessorConstants;

public class OldSchoolMeasures {
	
	/* ********** */
	/* Formatters */
	/* ********** */
	/** The formatter for double measures with at most 2 digits after the decimal separator. */
	public static final String DOUBLE_FORMATTER = "DOUBLE[#,###.00;-#,###.00]";
	/** The formatter for double measures with no number after the decimal separator. */
	public static final String DOUBLE_FORMATTER_NO_ZEROES = "DOUBLE[#,###.##]";
	/** The int formatter. */
	public static final String INT_FORMATTER = "INT[#,###]";
	/** The date formatters for timestamps. */
	public static final String TIMESTAMP_FORMATTER = "DATE[HH:mm:ss]";
	
	/* ******************* */
	/* Measures definition */
	/* ******************* */
	
	public static final String DELTA_SUM_PP_INT = "Delta.SUM.PP.INT";
	public static final String DELTA_SUM_PP = "Delta.SUM.PP";
	public static final String DELTA_SUM_PP_USD = "Delta.SUM.USD.PP";

	/**
	 * Adds all the measures to the cube builder.
	 *
	 * @param builder The builder to enrich with the measures.
	 *
	 * @return The builder with the new measures.
	 */
	public static IHasAtLeastOneMeasure build(final ICanStartBuildingMeasures builder) {
		return builder			
				.withAggregatedMeasure()
					.sum(RISK_BUCKET__VALUE)
					.withName(DELTA_SUM_PP_INT)
					.withFormatter(DOUBLE_FORMATTER)
				
				
				.withPostProcessor(DELTA_SUM_PP)
					.withPluginKey(DeltaSumVector.PLUGIN_KEY)
					.withUnderlyingMeasures(DELTA_SUM_PP_INT)
					.withFormatter(DOUBLE_FORMATTER)		

					;
	}

}
