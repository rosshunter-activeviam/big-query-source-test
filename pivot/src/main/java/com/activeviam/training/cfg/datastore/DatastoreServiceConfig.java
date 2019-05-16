/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of Quartet Financial Systems Limited. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.cfg.datastore;

import com.qfs.desc.IStoreSecurity;
import com.qfs.desc.IStoreSecurityBuilder;
import com.qfs.desc.impl.StoreSecurityBuilder;
import com.qfs.service.store.IDatastoreServiceConfiguration;
import com.quartetfs.fwk.format.IFormatter;
import com.quartetfs.fwk.format.IParser;

import static com.activeviam.training.cfg.security.SecurityConfig.ROLE_ADMIN;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ActiveViam
 *
 */
public class DatastoreServiceConfig implements IDatastoreServiceConfiguration {

	/** @see #getStoresSecurity() */
	protected Map<String, IStoreSecurity> storesSecurity;
	
	/** @see #getCustomParsers() */
	protected Map<String, Map<String, IParser<?>>> customParsers;

	/** @see #getCustomFormatters() */
	protected Map<String, Map<String, IFormatter>> customFormatters;

	/** Default query timeout for queries */
	protected static final long DEFAULT_QUERY_TIMEOUT = 30_000L;
	
	/**
	 * Constructor for {@link DatastoreServiceConfig}.
	 */
	public DatastoreServiceConfig() {
		
		// Security
		this.storesSecurity = new HashMap<>();
		
		// Formatters
		this.customFormatters = new HashMap<>();
		
		// Parsers
		this.customParsers = new HashMap<>();
	}
	
	protected IStoreSecurity storePermissions() {

		IStoreSecurityBuilder builder = StoreSecurityBuilder.startBuildingStoreSecurity()
				.withStoreReaders(ROLE_ADMIN);
		return builder.build();
	}

	@Override
	public Map<String, Map<String, IParser<?>>> getCustomParsers() {
		
		return this.customParsers;
	}

	@Override
	public Map<String, Map<String, IFormatter>> getCustomFormatters() {
		
		return this.customFormatters;
	}

	@Override
	public Map<String, IStoreSecurity> getStoresSecurity() {
		
		return this.storesSecurity;
	}

	@Override
	public long getDefaultQueryTimeout() {
		
		return DEFAULT_QUERY_TIMEOUT;
	}

}
