package com.activeviam.training.constants;

public class EnvironmentConstants {

	/**
	 * Optional system property to define the path of an application 'env'
	 * properties file.
	 *
	 * <p>
	 * The property value examples: "classpath:env.properties" or
	 * "file:/home/etc/env.properties" (on UNIX/Linux systems... Be careful on
	 * Windows, use syntax "file:///C:/Users/john/config/env.properties").
	 *
	 * @see #DEFAULT_APP_ENV_PROPS_FILE_PATH
	 */
	public static final String APP_PROPS_FILE_PATH_SYSPROP = "app.properties.file.path";
	public static final String LOCALPATH_PROPS_FILE_PATH_SYSPROP = "localpath.properties.file.path";
	public static final String LOCALPATH_VECTORISED_PROPS_FILE_PATH_SYSPROP = "localpath-vectorised.properties.file.path";

	/**
	 * Default application 'env' properties file used if
	 * {@link #APP_ENV_PROPS_FILE_PATH_SYSPROP} system property has not been
	 * explicitly set.
	 */
	public static final String DEFAULT_APP_PROPS_FILE_PATH = "classpath:app.properties";
	public static final String DEFAULT_LOCALPATH_PROPS_FILE_PATH = "classpath:localpath.properties";
	public static final String DEFAULT_LOCALPATH_VECTORISED_PROPS_FILE_PATH = "classpath:localpath-vectorised.properties";

}
