/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg;

import com.activeviam.training.cfg.security.SecurityConfig;
import com.qfs.content.service.IContentService;
import com.qfs.pivot.content.IActivePivotContentService;
import com.qfs.pivot.content.impl.ActivePivotContentServiceBuilder;
import com.qfs.server.cfg.content.IActivePivotContentServiceConfig;
import com.qfs.util.impl.QfsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Spring configuration of the ContentService.
 * 
 * @author ActiveViam
 *
 */
@Configuration
public class LocalContentServiceConfig implements IActivePivotContentServiceConfig {

	@Override
	@Bean
	public IContentService contentService() {
		return activePivotContentService().getContentService().getUnderlying();
	}


	@Override
	@Bean
	public IActivePivotContentService activePivotContentService() {
		final Properties hibernateProperties = QfsProperties.loadProperties("hibernate.properties");
		return new ActivePivotContentServiceBuilder()
				.withPersistence(new org.hibernate.cfg.Configuration().addProperties(hibernateProperties))
				.withAudit()
				.withoutCache()
				.needInitialization(SecurityConfig.ROLE_ADMIN, SecurityConfig.ROLE_ADMIN)
				.build();
	}
}
