/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.pivot.profiles;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.activeviam.training.cfg.datastore.VectorisedDatastoreDescriptionConfig;
import com.activeviam.training.cfg.pivot.vectorised.PivotManagerConfig;
import com.activeviam.training.cfg.source.LocalPathSourceConfig;
import com.activeviam.training.constants.EnvironmentConstants;

/**
 * @author ActiveViam
 */

@Profile(VectorisedConfig.SPRING_PROFILE)
@PropertySource(value = {
		"${" + EnvironmentConstants.LOCALPATH_VECTORISED_PROPS_FILE_PATH_SYSPROP + ":" + EnvironmentConstants.DEFAULT_LOCALPATH_VECTORISED_PROPS_FILE_PATH + "}"
})
@Configuration
@Import(value = {
        PivotManagerConfig.class,
        // Load vector files from the local file system
     	VectorisedDatastoreDescriptionConfig.class,
        LocalPathSourceConfig.class
})

public class VectorisedConfig {
    public static final String SPRING_PROFILE = "vectorised";
}