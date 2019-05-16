package com.activeviam.training.postprocessor.impl;

import java.util.Properties;

import com.qfs.vector.IVector;
import com.quartetfs.biz.pivot.ILocation;
import com.quartetfs.biz.pivot.cube.hierarchy.measures.IPostProcessorCreationContext;
import com.quartetfs.biz.pivot.postprocessing.IPostProcessor;
import com.quartetfs.biz.pivot.postprocessing.impl.ABasicPostProcessor;
import com.quartetfs.fwk.QuartetException;
import com.quartetfs.fwk.QuartetExtendedPluginValue;

@QuartetExtendedPluginValue(intf = IPostProcessor.class, key=DeltaSumVector.PLUGIN_KEY)
public class DeltaSumVector extends ABasicPostProcessor<Double> {
	
	private static final long serialVersionUID = 2512169396164146637L;
	public static final String PLUGIN_KEY = "DELTA_VECTOR_SUM";
	
	public DeltaSumVector(String name, IPostProcessorCreationContext creationContext) {
		super(name, creationContext);
	}

	@Override
	public void init(Properties properties) throws QuartetException {
		super.init(properties);
	}
	
	/**
	 * sum the vector of doubles
	 */
	@Override
	public Double evaluate(ILocation location, Object[] underlyingMeasures) {
		IVector sensiVector = (IVector) underlyingMeasures[0];
		return sensiVector.sumDouble();
	}

	@Override
	public String getType() {
		return PLUGIN_KEY;
	}

}
