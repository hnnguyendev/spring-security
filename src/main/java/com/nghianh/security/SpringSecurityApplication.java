package com.nghianh.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.nghianh.security.config.SwaggerConfig;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class SpringSecurityApplication extends SpringBootServletInitializer {

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(SpringSecurityJwtApplication.class);
//	}

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
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
