package com.svm.hackathon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 
 * This class configures the Model View Controllers for the application.
 *
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter{

	/**
	 * This bean sets up mappins for the view resolver on the configured servlet.
	 * 
	 * @return InternalResourceViewResolver
	 */
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver(){
		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/WEB-INF/");
		internalResourceViewResolver.setSuffix(".html");
		return internalResourceViewResolver;
	}
	
	/**
	 * Adds resource locations to the view resolver for static files that will be used by the client application.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry){
		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/js/");
		registry.addResourceHandler("/pics/**").addResourceLocations("/WEB-INF/pics/");
		registry.addResourceHandler("/html/**").addResourceLocations("/WEB-INF/html/");
		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/css/");
	}
	
	/**
	 * Adds the individual static views to a route on the servlet.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry){
		registry.addViewController("/").setViewName("html/index");
	}
	
	/**
	 * Enables this configuration on the servlet.
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
		configurer.enable();
	}
}
