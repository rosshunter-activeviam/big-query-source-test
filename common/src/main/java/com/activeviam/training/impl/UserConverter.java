/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training.impl;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Logback converter to retrieve the current user generating the log
 *
 * @author ActiveViam
 */
public class UserConverter extends ClassicConverter {

	@Override
	public String convert(final ILoggingEvent event) {
		String user;
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			user = "-";
		} else {
			user = auth.getName();
		}
		return user;
	}

}