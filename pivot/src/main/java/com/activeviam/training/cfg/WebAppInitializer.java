/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg;

import static org.springframework.security.config.BeanIds.SPRING_SECURITY_FILTER_CHAIN;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import com.activeviam.training.cfg.security.SecurityConfig;

/**
 * Initializer of the Web Application.
 * 
 * @author ActiveViam
 *
 */
public class WebAppInitializer implements WebApplicationInitializer {
	
	private static final Logger LOGGER = Logger.getLogger(WebAppInitializer.class.getSimpleName());
	private static final String ACTIVE_PROFILES_PROPERTY = AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		// make sure that JUL logs are all redirected to SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		propertiesCheck();
		
		
		// Spring Context Bootstrapping
		final AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
		rootAppContext.register(ApplicationConfig.class);
		servletContext.addListener(new ContextLoaderListener(rootAppContext));
		servletContext.getSessionCookieConfig().setName(SecurityConfig.COOKIE_NAME);

		// The main servlet/the central dispatcher
		final DispatcherServlet servlet = new DispatcherServlet(rootAppContext);
		servlet.setDispatchOptionsRequest(true);
		final Dynamic dispatcher = servletContext.addServlet("springDispatcherServlet", servlet);
		dispatcher.addMapping("/*");
		dispatcher.setLoadOnStartup(1);

		// Spring Security Filter
		final FilterRegistration.Dynamic springSecurity = servletContext.addFilter(SPRING_SECURITY_FILTER_CHAIN, new DelegatingFilterProxy());
		springSecurity.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
	}
	
	private static void propertiesCheck() {
		String activeprofiles = System.getProperty(ACTIVE_PROFILES_PROPERTY);
		if (activeprofiles == null || activeprofiles.length() == 0) {
			LOGGER.log(Level.SEVERE, "No Spring profiles set. Will fall back and use: [standard, localpath]. "
					+ "Please set the desired profiles as system properties if you wish to change this");
			activeprofiles = "standard,localpath";
		}
		
		System.setProperty(ACTIVE_PROFILES_PROPERTY, activeprofiles);
		LOGGER.log(Level.INFO, "Active Spring Profiles: " + activeprofiles);
	}

}
