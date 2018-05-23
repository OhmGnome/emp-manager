package com.svm.hackathon;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.svm.hackathon.config.WebMvcConfig;

/**
 * 
 * 
 * 
 * This class configures the dispatcher servlet and the routing for static web pages in the application. 
 *
 */
@Configuration
@PropertySource("classpath:/application.properties")
public class ApplicationInitializer implements WebApplicationInitializer {

	/**
	 * This method will run as the servers starts to configure the routing.
	 */
	@Override
	public void onStartup(ServletContext container) throws ServletException {
		
		// Loads an application context for class configuration.
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		
		// Configures the context based on the MVC configuration class.
		context.register(WebMvcConfig.class);
		
		// Creates a new servlet.
		DispatcherServlet servlet = new DispatcherServlet(context);
		ServletRegistration.Dynamic dispatcher = container.addServlet("dispatcher", servlet);
		
		// Adds mappings to the servlet.
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
