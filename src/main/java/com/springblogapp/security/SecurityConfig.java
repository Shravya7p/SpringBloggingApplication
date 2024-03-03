package com.springblogapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	public final String[] PUBLIC_URLS= {
			"/api/v1/auth/**",
			"/v3/api-docs",
			"/v2/api-docs",
			"/swagger-resources/**",
			"/swagger-ui/**",
			"/webjars/**"
	};
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		
        http
            .csrf(csrf->csrf.disable())
            .authorizeHttpRequests((authz) -> authz
//                .requestMatchers("/api/v1/auth/**").permitAll()
//                .requestMatchers("/v3/api-docs").permitAll()
            	.requestMatchers(PUBLIC_URLS).permitAll()	
                .requestMatchers(HttpMethod.GET).permitAll()
                
                .anyRequest().authenticated()
            )
            
            .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(this.jwtAuthenticationEntryPoint))
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults());
        
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
	@Bean
	 public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	 }
	 
	 @Bean
	 public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
		 
	 }
	 @Bean
	 public DaoAuthenticationProvider daoAuthenticationProvider() {
		 DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		 provider.setUserDetailsService(customUserDetailService);
		 provider.setPasswordEncoder(passwordEncoder());
		 return provider;
	 }
	
//	 @Bean
//	 public AuthenticationManager authenticationManagerBean() throws Exception{
//		 
//		 return super.authenticationManagerBean();
//	 }
	 
	 
//	 @Bean
//	 public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//	     return authenticationConfiguration.getAuthenticationManager();
//	 }
}
