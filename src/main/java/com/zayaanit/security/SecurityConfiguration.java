package com.zayaanit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired private UserDetailsService userDetailsService;

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

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests()
				.antMatchers(
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
				.antMatchers("/SA11/**").hasRole("ZADMIN")
				.antMatchers("/SA12/**").hasRole("ZADMIN")
				.antMatchers("/SA13/**").hasRole("ZADMIN")
				.antMatchers("/SA14/**").hasRole("ZADMIN")
				.antMatchers("/SA15/**").hasRole("ZADMIN")
				.antMatchers("/SA16/**").hasRole("ZADMIN")
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error")
				.permitAll()
				.defaultSuccessUrl("/")
			.and()
				.logout()
				.addLogoutHandler(logoutHandler())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID_ASPI")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				
				.logoutSuccessHandler(logoutSuccessHandler())
				.permitAll()
			.and()
				.exceptionHandling()
				.accessDeniedPage("/accessdenied")
			.and()
				.csrf().disable(); 
	}

	public SimpleAuthenticationFilter authenticationFilter() throws Exception {
		SimpleAuthenticationFilter filter = new SimpleAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return filter;
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}
}
