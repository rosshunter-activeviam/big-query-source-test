/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.datastore;

import com.qfs.multiversion.IEpoch;
import com.qfs.security.IBranchPermissionsManager;
import com.qfs.security.impl.BranchPermissions;
import com.qfs.security.impl.BranchPermissionsManager;
import com.qfs.server.cfg.IActivePivotBranchPermissionsManagerConfig;
import org.springframework.context.annotation.Bean;

import static com.activeviam.training.cfg.security.SecurityConfig.ROLE_ADMIN;

import java.util.Collections;

/**
 * @author ActiveViam
 *
 */
public class BranchPermissionsManagerConfig implements IActivePivotBranchPermissionsManagerConfig {

	@Override
	@Bean
	public IBranchPermissionsManager branchPermissionsManager() {

		final IBranchPermissionsManager branchPermissionsManager = new BranchPermissionsManager(
				Collections.singleton(ROLE_ADMIN),
				Collections.emptySet(),
				Collections.emptySet());
		
		final BranchPermissions permissions = new BranchPermissions(
				Collections.singleton(ROLE_ADMIN), 
				Collections.singleton(ROLE_ADMIN));
		
		branchPermissionsManager.setBranchPermissions(IEpoch.MASTER_BRANCH_NAME, permissions);
		
		return branchPermissionsManager;
	}

}
