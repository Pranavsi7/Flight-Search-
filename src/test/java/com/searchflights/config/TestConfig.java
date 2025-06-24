package com.searchflights.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
/**
 * Configuration class for test-specific Spring MVC setup.
 * <p>
 * Defines a {@link ViewResolver} bean for resolving JSP views during controller testing.
 * This configuration ensures that view names returned by controllers are mapped to
 * JSP files located under {@code /WEB-INF/views/} with a {@code .jsp} suffix.
 * </p>
 *
 * <p>
 * This class is typically used in test contexts where a lightweight Spring configuration
 * is needed to support view resolution without a full web server.
 * </p>
 *
 * @author Pranav Singh
 */

@Configuration
public class TestConfig {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
