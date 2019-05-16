/*
 * (C) Quartet FS 2010-2014
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.postprocessor.impl;

import com.quartetfs.biz.pivot.ILocation;
import com.quartetfs.biz.pivot.cube.hierarchy.measures.IPostProcessorCreationContext;
import com.quartetfs.biz.pivot.impl.LocationUtil;
import com.quartetfs.biz.pivot.postprocessing.IPostProcessor;
import com.quartetfs.fwk.QuartetExtendedPluginValue;

/**
 * <b>ForexPostProcessor</b>
 *
 * extends for the abstract class ADynamicAggregationPostProcessor.
 * @author Quartet FS
 */
@QuartetExtendedPluginValue(intf = IPostProcessor.class, key = FxWithCachePostProcessor.PLUGIN_KEY)
public class FxWithCachePostProcessor extends AFxPostprocessor {

	private static final long serialVersionUID = 3198390737386635653L;
	
	/** post processor plugin key */
	public static final String PLUGIN_KEY = "FX_WITH_CACHE";

	public FxWithCachePostProcessor(String name, IPostProcessorCreationContext creationContext) {
		super(name, creationContext);
	}

	/**
	 * Perform the evaluation of the post processor on a leaf (as defined in the properties).
	 * Here the leaf level is the Currency level in the Currency hierarchy .
	 */
	@Override
	protected Double evaluateLeaf(ILocation leafLocation, Object[] underlyingMeasures) {

		//retrieve the currency
		final String currency = (String) LocationUtil.getCoordinate(leafLocation, currencyLevelInfo);

		//retrieve the measure in the native currency
		final double measureNative = (Double) underlyingMeasures[0];


		// if currency is reference currency or measureNative is equal to 0.0 no need to convert
		if ((currency.equals(refCurrency)) || (measureNative == .0))
			return measureNative;


		Double rate = getRate(getDatastoreVersion(), currency, refCurrency);

		//compute equivalent in reference currency
		if (null == rate || RATE_NOT_FOUND == rate) {
			return null;
		}

		return measureNative * rate;
	}
	
	/**
	 * @return the type of this post processor, within the post processor extended plugin.
	 */
	@Override
	public String getType() {
		return PLUGIN_KEY;
	}
}
