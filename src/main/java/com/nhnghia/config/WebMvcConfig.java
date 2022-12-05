package com.nhnghia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nhnghia.common.Constants;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Value("${app.cors.allowedOrigins}")
	private String[] allowedOrigins;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(allowedOrigins)
				.allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
//        .allowedHeaders("*")
//        .allowCredentials(true)
				.maxAge(Constants.MAX_AGE_SECS);
	}
}
