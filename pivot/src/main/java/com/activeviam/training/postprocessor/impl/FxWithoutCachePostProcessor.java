/*
 * (C) Quartet FS 2010-2014
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.postprocessor.impl;


import java.util.concurrent.atomic.AtomicInteger;

import com.quartetfs.biz.pivot.ILocation;
import com.quartetfs.biz.pivot.cube.hierarchy.measures.IPostProcessorCreationContext;
import com.quartetfs.biz.pivot.impl.LocationUtil;
import com.quartetfs.biz.pivot.postprocessing.IPostProcessor;
import com.quartetfs.fwk.QuartetExtendedPluginValue;

/**
 * <b>ForexWithoutCachePostProcessor</b>
 *
 * extends for the abstract class ADynamicAggregationPostProcessor.
 * @author Quartet FS
 */
@QuartetExtendedPluginValue(intf = IPostProcessor.class, key = FxWithoutCachePostProcessor.PLUGIN_KEY)
public class FxWithoutCachePostProcessor extends AFxPostprocessor {

	private static final long serialVersionUID = 3198390737386635653L;
	
	/** post processor plugin key */
	public static final String PLUGIN_KEY = "FX_WITHOUT_CACHE";

	public FxWithoutCachePostProcessor(String name, IPostProcessorCreationContext creationContext) {
		super(name, creationContext);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * Perform the evaluation of the post processor on a leaf (as defined in the properties).
	 * Here the leaf level is the Currency level in the Currency hierarchy .
	 */
	@Override
	protected Double evaluateLeaf(ILocation leafLocation, Object[] underlyingMeasures) {
		// Uncomment this line to count the number of times we hit the evaluate method
		// System.out.println(leafCount.getAndIncrement());
		
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
