package com.activeviam.training.main;

import com.activeviam.training.cfg.ApplicationConfig;
import com.activeviam.training.cfg.pivot.PivotManagerConfig;
import com.activeviam.training.cfg.security.SecurityConfig;
import com.qfs.pivot.servlet.impl.ContextValueFilter;
import com.qfs.security.impl.SpringCorsFilter;
import com.qfs.servlet.impl.SqlDriverCleaner;
import org.apache.cxf.transport.http.Cookie;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.MultipartConfigElement;

/**
 * SpringBoot starter class for ActivePivot
 *
 * We don't use {@link SpringBootApplication} here because it will load too many beans including those we don't need and causing bean conflict
 *
 * @author ActiveViam
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan("com.activeviam.training.cfg")
@Import({ ApplicationConfig.class, PivotManagerConfig.class})
public class ActivePivotSpringbootApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ActivePivotSpringbootApplication.class, args);
    }

    /**IActivePivotManagerDescriptionConfig
     * Special beans to make AP work in SpringBoot https://github.com/spring-projects/spring-boot/issues/15373
     */
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistration(
            final DispatcherServlet dispatcherServlet,
            final ObjectProvider<MultipartConfigElement> multipartConfig) {
        final DispatcherServletRegistrationBean registration = new DispatcherServletRegistrationBean(
                dispatcherServlet, "/*");
        registration.setName("springDispatcherServlet");
        registration.setLoadOnStartup(1);
        multipartConfig.ifAvailable(registration::setMultipartConfig);
        return registration;
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.addListener(new SqlDriverCleaner());
            CookieGenerator cookie = new CookieGenerator();
            //.configure(servletContext.getSessionCookieConfig(), SecurityConfig.COOKIE_NAME);
        };
    }

    @Bean
    public FilterRegistrationBean<ContextValueFilter> disableRegisteringContextValueFilter(final ContextValueFilter filter) {
        final FilterRegistrationBean<ContextValueFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<SpringCorsFilter> disableRegisteringSpringCorsFilter(final SpringCorsFilter filter) {
        final FilterRegistrationBean<SpringCorsFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}