package com.zayaanit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import jakarta.servlet.DispatcherType;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public LogoutHandler logoutHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(provider);
	}

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.authenticationManager(authenticationManager)
			.securityContext(sc -> sc.securityContextRepository(securityContextRepository()))
			.addFilterBefore(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
				// Spring Security 5 only secured REQUEST dispatches; keep forwards and error pages open
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
				.requestMatchers(
					"/developer/**",
					"/login/fakelogin",
					"/login/directloginfragment",
					"/login/directloginfragment/**",
					"/login-assets/**",
					"/clearlogincache",
					"/api/**",
					"/swagger-ui.html",
					"/swagger-ui/**",
					"/favicon.ico",
					"/configuration/ui",
					"/webjars/**",
					"/configuration/security",
					"/swagger-resources/**",
					"/v2/api-docs/**",
					"/v2/rest/**",
					"/actuator/**"
				).permitAll()
				.requestMatchers("/SA11/**").hasRole("ZADMIN")
				.requestMatchers("/SA12/**").hasRole("ZADMIN")
				.requestMatchers("/SA13/**").hasRole("ZADMIN")
				.requestMatchers("/SA14/**").hasRole("ZADMIN")
				.requestMatchers("/SA15/**").hasRole("ZADMIN")
				.requestMatchers("/SA16/**").hasRole("ZADMIN")
				.requestMatchers("/SA17/**").hasRole("ZADMIN")
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.failureUrl("/login?error")
				.defaultSuccessUrl("/")
				.permitAll()
			)
			.logout(logout -> logout
				.addLogoutHandler(logoutHandler())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID_ASPI")
				.logoutRequestMatcher(PathPatternRequestMatcher.withDefaults().matcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.permitAll()
			)
			.exceptionHandling(ex -> ex.accessDeniedPage("/accessdenied"))
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}

	private SimpleAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) {
		SimpleAuthenticationFilter filter = new SimpleAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager);
		filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		// Since Spring Security 6 the login must be stored in the session explicitly
		filter.setSecurityContextRepository(securityContextRepository());
		return filter;
	}
}
