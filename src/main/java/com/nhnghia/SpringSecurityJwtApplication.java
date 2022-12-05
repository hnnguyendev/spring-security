package com.nhnghia;

import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.nhnghia.config.SwaggerConfig;
import com.nhnghia.entity.SystemRole;
import com.nhnghia.entity.SystemUser;
import com.nhnghia.service.AuthenticationService;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class SpringSecurityJwtApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringSecurityJwtApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

//	@Bean
//	CommandLineRunner run(AuthenticationService authenticationService) {
//		return args -> {
//			authenticationService.saveRole(new SystemRole(1L, "ROLE_USER"));
//			authenticationService.saveRole(new SystemRole(2L, "ROLE_ADMIN"));
//
//			authenticationService.signup(new SystemUser("nghianguyenhuu848@gmail.com", "123123", "Nghia", "Nguyen Huu",
//					null, false, new HashSet<>()));
//
//			authenticationService.addRoleToUser("nghianguyenhuu848@gmail.com", "ROLE_USER");
//			authenticationService.addRoleToUser("nghianguyenhuu848@gmail.com", "ROLE_ADMIN");
//
//		};
//	}

}
