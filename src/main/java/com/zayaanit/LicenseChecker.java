package com.zayaanit;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.zayaanit.config.AppConfig;
import com.zayaanit.util.LicenseManager;

/**
 * @author Zubayer Ahamed
 * @since Dec 19, 2023
 */
@Profile(value = { "lvrcl" })
@Component
public class LicenseChecker implements ApplicationRunner {

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		System.out.println(appConfig.getLicenseKey());

		String secretKey = "zubayer%123";

//		System.out.println("=== GENERATING LICENSE ===");

		Map<String, String> props = new HashMap<>();
		props.put("PRODUCT", "ASPI");
		props.put("CUSTOMER", "Sadman");
		props.put("VERSION", "2.0");
		props.put("LICENSE-TYPE", "PREMIUM");
		props.put("USERS", "50");

		Date expiry = LicenseManager.addDays(60); // 60 days

		LicenseManager.LicenseData license = LicenseManager.generateLicense(props, expiry, secretKey);
		System.out.println(license.getLicenseKey());

		if (!license.getLicenseKey().equalsIgnoreCase(appConfig.getLicenseKey())) {
			System.out.println("====================> ******* Invalid license key ******* <==============");
			int exitCode = SpringApplication.exit(applicationContext, () -> 1);
			System.exit(exitCode);
		}

		Map<String, String> validation = LicenseManager.validateLicense(appConfig.getLicenseKey(), secretKey);

		System.out.println("Validation Result:");
		System.out.println("Status: " + validation.get("STATUS"));
		System.out.println("Expiry: " + validation.get("EXPIRY_DATE"));

		if(validation.get("STATUS").equalsIgnoreCase("EXPIRED")) {
			System.out.println("====================> ******* Invalid license key ******* <==============");
			int exitCode = SpringApplication.exit(applicationContext, () -> 1);
			System.exit(exitCode);
		}

		System.out.println("\nAll Properties:");
		for (Map.Entry<String, String> entry : validation.entrySet()) {
			if (!entry.getKey().equals("STATUS") && !entry.getKey().equals("EXPIRY_DATE")) {
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
		}

	}
}
