package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Autowired
	private AccountDetailsService accountDetailsService;
  
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
  
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(accountDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder()); 
		return authProvider;
	}
  
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
//	@Bean
//	public MemberDetailsService memberDetailsService() {
//		return new MemberDetailsService();
//	}
//
//	@Bean
//	public BCryptPasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//		authProvider.setUserDetailsService(memberDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//		return authProvider;
//	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				(requests) -> requests
					
						.requestMatchers("/bootstrap/*/*").permitAll()
						.requestMatchers("/Image/*").permitAll()
						.requestMatchers("/homepage").permitAll()
						.requestMatchers("/images/*","/home").permitAll()
						.requestMatchers("/programrun").hasRole("ADMIN")
						.requestMatchers("/trainers/programrun").hasRole("TRAINER")
						.requestMatchers("/programrun/add", "/programrun/save").hasRole("ADMIN")
						.requestMatchers("/timesheet/add").hasRole("ADMIN")
						.requestMatchers("/trainers", "/programrun/edit/*").permitAll()
						.requestMatchers("/range/report").hasAnyRole("TRAINER","ADMIN")
						.requestMatchers("/timesheet/edit", "/programrun/delete/*").hasRole("TRAINER")
						.requestMatchers("/adminAccount/add", "/adminAccount/edit/*", "/adminAccount/save", "/adminAccount/delete/*").hasRole("ADMIN")
						.requestMatchers("/adminAccount").hasRole("ADMIN")
						.requestMatchers("/signup", "/signup/save", "/forgotPassword", "/resetPassword", "/verify").permitAll()
					    .requestMatchers("/trainerAccount", "/trainerAccount/edit/*", "/trainerAccount/save", "/trainerAccount/delete/*").hasRole("TRAINER")
						.anyRequest().authenticated())
				.formLogin((form) -> form.loginPage("/login").permitAll().defaultSuccessUrl("/", true))
				.logout((logout) -> logout.logoutUrl("/logout").permitAll())
				.exceptionHandling().accessDeniedPage("/403")
				.and()
			    .exceptionHandling().accessDeniedPage("/403")
				.and()
		        .rememberMe()
		            .key("abcdefg123456")
		            .tokenValiditySeconds(3 * 24 * 60 * 60)
		            .rememberMeServices(rememberMeServices())
		            .rememberMeParameter("remember-me")
		            .and(); ;

		return http.build();
	}
	@Bean
	public RememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("abcdefg123456", accountDetailsService);
		rememberMeServices.setTokenValiditySeconds(3 * 24 * 60 * 60);
		rememberMeServices.setAlwaysRemember(false);
		return rememberMeServices;
		
	}
}
