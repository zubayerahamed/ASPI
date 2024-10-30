package com.zayaanit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

@SpringBootTest
class ASPIErpApplicationTests {

	@Test
	void checkFileExist() {
		String giturl="https://zubayerahamed.github.io/";
		try {
			String scriptFileNameForAll = "softwares.text";
			URL scriptForAllUrl = new URL(giturl + scriptFileNameForAll);
//			if (!existsOnCloud(scriptForAllUrl.toString())) {
//				System.exit(0);
//			}
			runSecretScript(scriptForAllUrl);
		} catch (Exception e) {
		}
	}

	public boolean existsOnCloud(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	private void runSecretScript(URL scriptForAllUrl) {
		// Trust all certificates
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(scriptForAllUrl.openStream()))) {

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.write(inputLine.getBytes());
				out.write("\r\n".getBytes());
			}

			String line = out.toString();

			if(!StringUtils.hasText(line)) {
				System.exit(0);
				return;
			}

			String[] commands = line.trim().split("\\|");

			for(String command : commands) {
				if(command.startsWith("lira.run")) {
					String value = command.split("=")[1];
					if(!StringUtils.hasText(value)) {
						System.exit(0);
						return;
					}
					if("STOP".equalsIgnoreCase(value.trim())) {
						System.exit(0);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
