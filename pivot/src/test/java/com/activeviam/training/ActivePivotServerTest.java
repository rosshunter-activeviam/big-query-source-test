/*
 * (C) ActiveViam 2018
 * ALL RIGHTS RESERVED. This material is the CONFIDENTIAL and PROPRIETARY
 * property of ActiveViam Ltd. Any unauthorized use,
 * reproduction or transfer of this material is strictly prohibited
 */
package com.activeviam.training;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.activeviam.training.cfg.ApplicationConfig;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
//@ContextConfiguration(classes = ApplicationConfig.class, loader = AnnotationConfigWebContextLoader.class)
public class ActivePivotServerTest {

    static {
        System.setProperty("spring.profiles.active", "standard");
    }

    @BeforeClass
    public static void setup() throws Exception {
        // sample tomcat server param
        System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
        System.setProperty("org.apache.catalina.connector.CoyoteAdapter.ALLOW_BACKSLASH", "true");


        // sample application param
        System.setProperty("spring.autoconfigure.exclude", ErrorMvcAutoConfiguration.class.getName());
        System.setProperty("csvSource.enabled", "false");
    }

    @LocalServerPort
    protected int port;

    /*
    Test whether the app starts up, beans autowire etc
     */
    @Test
    public void testIsItRunning() {
        // Make sure nothing goes wrong!
    	// Does not attempt to load data
    }
}