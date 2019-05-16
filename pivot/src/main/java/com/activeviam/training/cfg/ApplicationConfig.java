/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg;

import com.qfs.server.cfg.IDatastoreDescriptionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.activeviam.training.cfg.datastore.BranchPermissionsManagerConfig;
import com.activeviam.training.cfg.pivot.profiles.StandardConfig;
import com.activeviam.training.cfg.pivot.profiles.VectorisedConfig;
import com.activeviam.training.cfg.security.ActivePivotCorsFilterConfig;
import com.activeviam.training.cfg.security.SecurityConfig;
import com.activeviam.training.constants.EnvironmentConstants;
import com.qfs.content.cfg.impl.ContentServerResourceServerConfig;
import com.qfs.content.cfg.impl.ContentServerWebSocketServicesConfig;
import com.qfs.pivot.content.impl.DynamicActivePivotContentServiceMBean;
//import com.qfs.pivot.monitoring.impl.MemoryMonitoringService;
import com.qfs.server.cfg.IDatastoreConfig;
import com.qfs.server.cfg.content.IActivePivotContentServiceConfig;
import com.qfs.server.cfg.i18n.impl.LocalI18nConfig;
import com.qfs.server.cfg.impl.ActivePivotConfig;
import com.qfs.server.cfg.impl.ActivePivotRemotingServicesConfig;
import com.qfs.server.cfg.impl.ActivePivotServicesConfig;
import com.qfs.server.cfg.impl.ActivePivotWebServicesConfig;
import com.qfs.server.cfg.impl.ActivePivotWebSocketServicesConfig;
import com.qfs.server.cfg.impl.ActivePivotXmlaServletConfig;
import com.qfs.server.cfg.impl.ActiveViamRestServicesConfig;
import com.qfs.server.cfg.impl.DatastoreConfig;
import com.qfs.server.cfg.impl.JwtConfig;
import com.qfs.service.store.impl.NoSecurityDatastoreServiceConfig;
import com.quartetfs.biz.pivot.monitoring.impl.DynamicActivePivotManagerMBean;
import com.quartetfs.fwk.Registry;
import com.quartetfs.fwk.contributions.impl.ClasspathContributionProvider;
import com.quartetfs.fwk.monitoring.jmx.impl.JMXEnabler;

/**
 * Spring configuration of the ActivePivot Application services
 *  
 * @author ActiveViam
 *
 */
@PropertySource(value = {"classpath:jwt.properties",
		"classpath:content.service.properties",
		"classpath:app.properties",
		"${" + EnvironmentConstants.APP_PROPS_FILE_PATH_SYSPROP + ":" + EnvironmentConstants.DEFAULT_APP_PROPS_FILE_PATH + "}"
		})
@Configuration
@Import(value = {
        ActivePivotConfig.class,
        DatastoreConfig.class,
        NoSecurityDatastoreServiceConfig.class,
        BranchPermissionsManagerConfig.class,
        LocalContentServiceConfig.class,
        SecurityConfig.class,
        ActivePivotCorsFilterConfig.class,
        
        ActivePivotServicesConfig.class,
		ActivePivotWebSocketServicesConfig.class,
		ContentServerWebSocketServicesConfig.class,
        ContentServerResourceServerConfig.class,
		ActiveViamRestServicesConfig.class,
		JwtConfig.class,
		
		ActivePivotWebServicesConfig.class,
		ActivePivotXmlaServletConfig.class,
		ActivePivotRemotingServicesConfig.class,
		
		// Pivot Configs
		StandardConfig.class,
		VectorisedConfig.class,
		
		// UI Resource Statement
		ActiveUIResourceServerConfig.class,
		
		// I18 stuff needed to make the UI work
		LocalI18nConfig.class
		
        })
public class ApplicationConfig {

	/* Before anything else we statically initialize the Quartet FS Registry. */
    static {
        Registry.setContributionProvider(new ClasspathContributionProvider("com.qfs", "com.quartetfs", "com.activeviam"));
    }

	@Autowired
	protected ActivePivotConfig apConfig;
	
	 /**
     * ActivePivot content service spring configuration
     */
    @Autowired
    protected IActivePivotContentServiceConfig apCSConfig;

	@Autowired
	protected IDatastoreConfig datastoreConfig;

	@Autowired
	protected ActivePivotServicesConfig apServiceConfig;

	@Autowired
	protected SecurityConfig securityConfig;
	
	@Autowired
	private Environment env;
	

	/**
     * Enable JMX Monitoring for the Datastore
     *
     * @return the {@link JMXEnabler} attached to the datastore
     */
    @Bean
    public JMXEnabler JMXDatastoreEnabler() {
        return new JMXEnabler(datastoreConfig.datastore());
    }

    /**
     * Enable JMX Monitoring for ActivePivot Components
     *
     * @return the {@link JMXEnabler} attached to the activePivotManager
     */
    @Bean
    @DependsOn(value = "startManager")
    public JMXEnabler JMXActivePivotEnabler() {
        return new JMXEnabler(new DynamicActivePivotManagerMBean(apConfig.activePivotManager()));
    }

    /**
     * Enable JMX Monitoring for the Content Service
     *
     * @return the {@link JMXEnabler} attached to the content service.
     */
    @Bean
    public JMXEnabler JMXActivePivotContentServiceEnabler() {
        // to allow operations from the JMX bean
        return new JMXEnabler(
                new DynamicActivePivotContentServiceMBean(
                        apCSConfig.activePivotContentService(),
                        apConfig.activePivotManager()));
    }
    
//    @Bean
//    public JMXEnabler JMXDirectMemoryMonitoringEnabler() {
//      return new JMXEnabler(new MemoryMonitoringService(datastoreConfig.datastore()));
//   }


	/**
	 *
	 * Initialize and start the ActivePivot Manager, after performing all the injections into the ActivePivot plug-ins.
	 *
	 * @return void
	 * @throws Exception any exception that occurred during the injection, the initialization or the starting
	 */
	@Bean
	public Void startManager() throws Exception {

		/* ********************************************************************** */
		/* Inject dependencies before the ActivePivot components are initialized. */
		/* ********************************************************************** */
		apManagerInitPrerequisitePluginInjections();

		/* *********************************************** */
		/* Initialize the ActivePivot Manager and start it */
		/* *********************************************** */
		apConfig.activePivotManager().init(null);
		apConfig.activePivotManager().start();

		return null;
	}
	
	/**
	 * Extended plugin injections that are required before doing the startup of the
	 * ActivePivot manager.
	 *
	 * @see #startManager()
	 * @throws Exception any exception that occurred during the injection
	 */
	protected void apManagerInitPrerequisitePluginInjections() throws Exception {
		/* ********************************************************* */
		/* Core injections for distributed architecture (when used). */
		/* ********************************************************* */
		
		// If we need this we can add it back again
	}

}
