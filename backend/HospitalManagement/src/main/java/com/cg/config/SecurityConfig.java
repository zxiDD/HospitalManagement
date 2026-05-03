package com.cg.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cg.security.JWTAuthFilter;

@Configuration
public class SecurityConfig {

	@Autowired
	private JWTAuthFilter jwtAuthFilter;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH", "PUT", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/login", "/auth/register", "/swagger-ui.html", "/swagger-ui/**",
								"/swagger-resources/**", "/v3/api-docs/**", "/actuator/**")
						.permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/physicians/**", "/nurses/**", "/departments/**",
								"/medications/**", "/procedures/**")
						.hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/physicians/**", "/nurses/**", "/departments/**",
								"/medications/**", "/procedures/**")
						.hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/physicians/**", "/nurses/**", "/departments/**",
								"/medications/**", "/procedures/**")
						.hasRole("ADMIN").requestMatchers("/prescribes/patient/**").hasAnyRole("PATIENT", "ADMIN")
						.requestMatchers("/prescribes/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/patients/**").authenticated()
						.requestMatchers(HttpMethod.POST, "/appointments/**").hasAnyRole("PATIENT", "ADMIN")
						.requestMatchers(HttpMethod.GET, "/appointments/**").hasAnyRole("PATIENT", "ADMIN")
						.requestMatchers("/patients/**", "/appointments/**", "/undergoes/**", "/stays/**", "/rooms/**",
								"/blocks/**")
						.hasAnyRole("PATIENT", "ADMIN")
						.requestMatchers(HttpMethod.GET, "/physicians/**", "/nurses/**", "/departments/**",
								"/medications/**", "/procedures/**")
						.authenticated().anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
