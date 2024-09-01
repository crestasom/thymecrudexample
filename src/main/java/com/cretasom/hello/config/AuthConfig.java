package com.cretasom.hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {
//	@Value("${jwt.set.uri}")
//	private String JWK_SET_URI;
	private Logger logger = LoggerFactory.getLogger(getClass());
//	@Autowired
//	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//	@Autowired
//	private CustomAuthenticationSuccessHandler successHandler;
//	@Autowired
//	private CustomAuthenticationFailureHandler failureHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						requests -> requests.requestMatchers("/login", "/validatelogin/**", "/css/**", "image/**")
								.permitAll().requestMatchers("/hotel/**", "/user/**").hasRole("ADMIN")
								.requestMatchers("/rating/**").hasRole("USER").anyRequest().authenticated())

				.formLogin(f -> f.loginPage("/login")).build();
	}

	@SuppressWarnings("deprecation")
	@Bean
	static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("adminpass").roles("ADMIN").and().withUser("user")
				.password("userpass").roles("USER");
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
