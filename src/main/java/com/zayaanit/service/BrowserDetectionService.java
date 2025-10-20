package com.zayaanit.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

/**
 * @author Zubayer Ahaned
 * @since Oct 20, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class BrowserDetectionService {

	public BrowserInfo getBrowserInfo(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String clientIp = getClientIpAddress(request);
		if (userAgent == null) {
			return new BrowserInfo("Unknown", "Unknown", "Unknown", "Unknown", userAgent, clientIp);
		}

		String browserName = detectBrowserName(userAgent);
		String browserVersion = detectBrowserVersion(userAgent, browserName);
		String os = detectOperatingSystem(userAgent);
		String deviceType = detectDeviceType(userAgent);

		return new BrowserInfo(browserName, browserVersion, os, deviceType, userAgent, clientIp);
	}

	/**
	 * Get client IP address considering proxies (X-Forwarded-For header)
	 */
	public String getClientIpAddress(HttpServletRequest request) {
		// Check X-Forwarded-For header first (for proxies, load balancers)
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
			// X-Forwarded-For can contain multiple IPs, the first one is the original
			// client
			String[] ips = xForwardedFor.split(",");
			return ips[0].trim();
		}

		// Check X-Real-IP header
		String xRealIp = request.getHeader("X-Real-IP");
		if (xRealIp != null && !xRealIp.isEmpty()) {
			return xRealIp.trim();
		}

		// Check CF-Connecting-IP (Cloudflare)
		String cfConnectingIp = request.getHeader("CF-Connecting-IP");
		if (cfConnectingIp != null && !cfConnectingIp.isEmpty()) {
			return cfConnectingIp.trim();
		}

		// Check True-Client-IP (Akamai)
		String trueClientIp = request.getHeader("True-Client-IP");
		if (trueClientIp != null && !trueClientIp.isEmpty()) {
			return trueClientIp.trim();
		}

		// Fallback to remote address
		return request.getRemoteAddr();
	}

	private String detectBrowserName(String userAgent) {
		String ua = userAgent.toLowerCase();

		if (ua.contains("firefox")) {
			return "Firefox";
		} else if (ua.contains("edg/") || ua.contains("edge/")) {
			return "Microsoft Edge";
		} else if (ua.contains("opr/") || ua.contains("opera")) {
			return "Opera";
		} else if (ua.contains("chrome") && !ua.contains("edg/") && !ua.contains("edge/")) {
			return "Chrome";
		} else if (ua.contains("safari") && !ua.contains("chrome")) {
			return "Safari";
		} else if (ua.contains("samsung")) {
			return "Samsung Internet";
		} else if (ua.contains("ucbrowser")) {
			return "UC Browser";
		}

		return "Unknown";
	}

	private String detectBrowserVersion(String userAgent, String browserName) {
		String ua = userAgent.toLowerCase();
		String version = "Unknown";

		switch (browserName.toLowerCase()) {
		case "chrome":
			version = extractVersion(ua, "chrome/");
			break;
		case "firefox":
			version = extractVersion(ua, "firefox/");
			break;
		case "safari":
			version = extractVersion(ua, "version/");
			if ("Unknown".equals(version)) {
				// Safari often doesn't have version/ in mobile
				Pattern pattern = Pattern.compile("safari/([0-9]+(?:\\.[0-9]+)*)");
				Matcher matcher = pattern.matcher(ua);
				if (matcher.find()) {
					version = matcher.group(1);
				}
			}
			break;
		case "microsoft edge":
			version = extractVersion(ua, "edg/");
			if ("Unknown".equals(version)) {
				version = extractVersion(ua, "edge/");
			}
			break;
		case "opera":
			version = extractVersion(ua, "opr/");
			if ("Unknown".equals(version)) {
				version = extractVersion(ua, "version/");
			}
			break;
		case "samsung internet":
			version = extractVersion(ua, "samsungbrowser/");
			break;
		}

		return version;
	}

	private String detectOperatingSystem(String userAgent) {
		String ua = userAgent.toLowerCase();

		// Mobile OS detection first (more specific)
		if (ua.contains("android")) {
			return "Android";
		} else if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod")) {
			return "iOS";
		} else if (ua.contains("windows phone")) {
			return "Windows Mobile";
		}

		// Desktop OS detection
		else if (ua.contains("windows nt 10") || ua.contains("windows 10")) {
			return "Windows 10";
		} else if (ua.contains("windows nt 11") || ua.contains("windows 11")) {
			return "Windows 11";
		} else if (ua.contains("windows nt 6.3")) {
			return "Windows 8.1";
		} else if (ua.contains("windows nt 6.2")) {
			return "Windows 8";
		} else if (ua.contains("windows nt 6.1")) {
			return "Windows 7";
		} else if (ua.contains("windows nt 6.0")) {
			return "Windows Vista";
		} else if (ua.contains("windows nt 5.1") || ua.contains("windows xp")) {
			return "Windows XP";
		} else if (ua.contains("mac os x") || ua.contains("macintosh")) {
			// Extract macOS version
			Pattern pattern = Pattern.compile("mac os x (10[._][0-9]+(?:[._][0-9]+)*)");
			Matcher matcher = pattern.matcher(ua);
			if (matcher.find()) {
				return "macOS " + matcher.group(1).replace('_', '.');
			}
			pattern = Pattern.compile("mac os x (1[1-9][._][0-9]*)");
			matcher = pattern.matcher(ua);
			if (matcher.find()) {
				return "macOS " + matcher.group(1).replace('_', '.');
			}
			return "macOS";
		} else if (ua.contains("linux")) {
			if (ua.contains("ubuntu")) {
				return "Ubuntu Linux";
			} else if (ua.contains("fedora")) {
				return "Fedora Linux";
			} else if (ua.contains("debian")) {
				return "Debian Linux";
			} else if (ua.contains("centos")) {
				return "CentOS Linux";
			}
			return "Linux";
		} else if (ua.contains("cros")) {
			return "Chrome OS";
		}

		return "Unknown";
	}

	private String detectDeviceType(String userAgent) {
		String ua = userAgent.toLowerCase();

		if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") || ua.contains("ipod")
				|| ua.contains("blackberry") || ua.contains("windows phone")) {
			return "Mobile";
		} else if (ua.contains("tablet") || ua.contains("ipad") || ua.contains("kindle") || ua.contains("silk")
				|| (ua.contains("android") && !ua.contains("mobile"))) {
			return "Tablet";
		} else {
			return "Desktop";
		}
	}

	private String extractVersion(String userAgent, String key) {
		Pattern pattern = Pattern.compile(key + "([0-9]+(?:\\.[0-9]+)*)");
		Matcher matcher = pattern.matcher(userAgent);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return "Unknown";
	}

	public static class BrowserInfo {
		private final String browserName;
		private final String browserVersion;
		private final String operatingSystem;
		private final String deviceType;
		private final String userAgent;
		private final String clientIp;

		public BrowserInfo(String browserName, String browserVersion, String operatingSystem, String deviceType,
				String userAgent, String clientIp) {
			this.browserName = browserName;
			this.browserVersion = browserVersion;
			this.operatingSystem = operatingSystem;
			this.deviceType = deviceType;
			this.userAgent = userAgent;
			this.clientIp = clientIp;
		}

		// Getters
		public String getBrowserName() {
			return browserName;
		}

		public String getBrowserVersion() {
			return browserVersion;
		}

		public String getOperatingSystem() {
			return operatingSystem;
		}

		public String getDeviceType() {
			return deviceType;
		}

		public String getUserAgent() {
			return userAgent;
		}

		public String getClientIp() {
			return clientIp;
		}

		@Override
		public String toString() {
			return String.format("Browser: %s %s, OS: %s, Device: %s", browserName, browserVersion, operatingSystem, deviceType);
		}
	}
}
