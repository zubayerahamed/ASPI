package com.zayaanit;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

//@SpringBootTest
class DemoErpApplicationTests {

	@Test
	void checkFileExist() {
		String fileName = "D:\\cr_live_reports\\aradjdt.rpt\\";
		
		
		
		File file = new File(fileName);
		System.out.println(file.exists());
		
		int lastIndex = fileName.lastIndexOf("\\");
		if (lastIndex != -1) {
			fileName = fileName.substring(0, lastIndex) + fileName.substring(lastIndex + 1);
        }
		
		System.out.println(fileName);
		
	}

	@Test
	void contextLoads() {

		String input = "Sample text &lt;EmailQuestionURL_eng&gt; More &lt;EmailQuestionURL_dutch&gt; text";

		// Define the regex pattern
		String pattern = "&lt;EmailQuestionURL(.*?)&gt;";

		// Compile the pattern
		Pattern p = Pattern.compile(pattern);

		// Create a matcher with the input string
		Matcher m = p.matcher(input);

		// Check if any match is found
		if (m.find()) {
			int startIndex = m.start();
			int endIndex = m.end();
			String replacableText = input.substring(startIndex, endIndex);
			System.out.println(replacableText);

			String providedDatePattern = m.group(1);
			System.out.println(providedDatePattern);

			System.out.println("String contains the pattern.");
		} else {
			System.out.println("String does not contain the pattern.");
		}
	}

	
	private String dowork(String input) {
		
		// Define the regex pattern
		String pattern = "&lt;EmailQuestionURL(.*?)&gt;";

		// Compile the pattern
		Pattern p = Pattern.compile(pattern);

		// Create a matcher with the input string
		Matcher m = p.matcher(input);

		// Check if any match is found
		if (m.find()) {
			int startIndex = m.start();
			int endIndex = m.end();
			String replacableText = input.substring(startIndex, endIndex);
			System.out.println(replacableText);

			String providedDatePattern = m.group(1);
			System.out.println(providedDatePattern);

			System.out.println("String contains the pattern.");
		} else {
			System.out.println("String does not contain the pattern.");
		}
		return input;
	}
}
