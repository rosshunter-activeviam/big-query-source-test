/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.pivot;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.activeviam.builders.StartBuilding;
import com.activeviam.desc.build.ICanBuildCubeDescription;
import com.activeviam.desc.build.ICubeDescriptionBuilder.INamedCubeDescriptionBuilder;
import com.qfs.desc.IDatastoreSchemaDescription;
import com.qfs.server.cfg.IActivePivotManagerDescriptionConfig;
import com.quartetfs.biz.pivot.context.impl.QueriesTimeLimit;
import com.quartetfs.biz.pivot.definitions.IActivePivotInstanceDescription;
import com.quartetfs.biz.pivot.definitions.IActivePivotManagerDescription;

/**
 * @author ActiveViam
 */

public class PivotManagerConfig implements IActivePivotManagerDescriptionConfig {
	
	/* *********************/
	/* OLAP Property names */
	/* *********************/
	
	public static final String MANAGER_NAME = "APManager";
	public static final String CATALOG_NAME = "APCatalog";
	public static final String SCHEMA_NAME = "APSchema";
	public static final String CUBE_NAME = "RiskCube";
	
	/* **************************************** */
	/* Levels, hierarchies and dimensions names */
	/* **************************************** */

	
	/* ********** */
	/* Formatters */
	/* ********** */
	public static final String DOUBLE_FORMATTER_ONE_DECIMAL = "DOUBLE[##.#]";
	public static final String INT_FORMATTER = "INT[#,###]";
	public static final String NATIVE_MEASURES = "Native Measures";
	
	/** The datastore schema {@link IDatastoreSchemaDescription description}. */
	@Autowired
	protected IDatastoreSchemaDescription datastoreDescription;

	
	@Override
	@Bean(name = "allinone")
	public IActivePivotManagerDescription managerDescription() {
		
		return StartBuilding.managerDescription(MANAGER_NAME)
				.withCatalog(CATALOG_NAME)
				.containingAllCubes()
				.withSchema(SCHEMA_NAME)
				.withSelection(Schema.createSchemaSelectionDescription(this.datastoreDescription))
				.withCube(createCubeDescription())
				.build();
	}
	

	/**
	 * Configures the given builder in order to created the cube description.
	 *
	 * @param builder The builder to configure
	 * @return The configured builder
	 */
	public static ICanBuildCubeDescription<IActivePivotInstanceDescription> configureCubeBuilder(
			final INamedCubeDescriptionBuilder builder){
		
		return builder
				.withContributorsCount()
					.withinFolder(NATIVE_MEASURES)
					.withAlias("Count")
					.withFormatter(INT_FORMATTER)
				.withUpdateTimestamp()
					.withinFolder(NATIVE_MEASURES)
					
				.withMeasures(OldSchoolMeasures::build)
										
				.withDimensions(Dimensions::build)
				
                // Aggregate provider
    			.withAggregateProvider()
					.jit()
					.withoutRangeSharing() // have to do because of stupid copper bug
				
                // Shared context values
                // Query maximum execution time (before timeout cancellation): 1h							
                .withSharedContextValue(QueriesTimeLimit.of(3600, TimeUnit.SECONDS))
                .withSharedDrillthroughProperties()
                .withMaxRows(10000)
                .end()
                
                
                // Add CoPPeR stuff
				.withDescriptionPostProcessor(
						StartBuilding.copperCalculations()
							.withDefinition(CopperCalculations::buildCalculations)
							.build()
				)
                ;
	}

	protected IActivePivotInstanceDescription createCubeDescription() {
		return configureCubeBuilder(StartBuilding.cube(CUBE_NAME)).build();
	}

}
