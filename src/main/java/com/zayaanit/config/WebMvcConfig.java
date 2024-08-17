package com.zayaanit.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.zayaanit.entity.Xscreens;
import com.zayaanit.interceptor.MenuAccessAuthorizationInterceptor;
import com.zayaanit.repository.XscreensRepo;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired private XscreensRepo xscreenRepo;

	@Bean
	public MenuAccessAuthorizationInterceptor menuAccessInterceptor() {
		return new MenuAccessAuthorizationInterceptor();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(menuAccessInterceptor()).addPathPatterns(getMenuPaths()); 
		registry.addInterceptor(localeChangeInterceptor());
	}

	private String[] getMenuPaths() {
		List<Xscreens> list = xscreenRepo.findAll();
		list = list.stream().distinct().filter(l -> l.getXtype().equalsIgnoreCase("Screen")).collect(Collectors.toList());

		List<String> paths = new ArrayList<>();
		for(Xscreens screen : list) {
			paths.add("/" + screen.getXscreen() + "/**");
		}
		return paths.toArray(new String[paths.size()]);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(
				"classpath:/messages/messages",
				"classpath:/messages/messages-salesandinvoice"
		);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
