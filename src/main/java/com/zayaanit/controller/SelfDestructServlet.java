package com.zayaanit.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.config.AppConfig;
import com.zayaanit.enums.ServiceStatus;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

/**
 * @author Zubayer Ahamed
 * @since Jul 23, 2023
 */
@Controller
@RequestMapping("/self-destruct")
public class SelfDestructServlet {

	@Autowired
	AppConfig appConfig;

	@GetMapping
	@ResponseBody
	public void selfDestruct(HttpServletRequest request, HttpServletResponse response)
			throws IOException, InterruptedException {

		SystemInfo systemInfo = new SystemInfo();
		OperatingSystem os = systemInfo.getOperatingSystem();

		boolean serviceRunning = false;
		boolean serviceFound = true;
		if ("Windows".equalsIgnoreCase(os.getFamily())) {
			Process process = new ProcessBuilder("C:\\Windows\\System32\\sc.exe", "query", appConfig.getTomcatServiceName()).start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String line;
			String scOutput = "";
			while ((line = br.readLine()) != null) {
				scOutput += line + "\n";
			}

			if(scOutput.contains("The specified service does not exist as an installed service.")) {
				serviceFound = false;
			}

			if(!serviceFound) {
				System.out.println("Service not found...");
				return;
			}

			serviceRunning = scOutput.contains(ServiceStatus.RUNNING.name());

			br.close();
			process.waitFor();

			process.destroy();
		} else {
			String command = "systemctl is-active " + appConfig.getTomcatServiceName();
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String scOutput = br.readLine();

			serviceRunning = scOutput != null && scOutput.equals("active");

			br.close();
			process.waitFor();
	
			process.destroy();
		}

		System.out.println("Tomcat server is running : " + serviceRunning);

		if (serviceRunning)
			return;

		try {
			// Create a batch file content
			String batchContent = "@echo off \n" + " \n"
					+ "REM Stop Tomcat service (replace \"TomcatServiceName\" with your actual Tomcat service name) \n"
					+ "net stop Tomcat9 \n" + " \n"
					+ "REM Wait for a few seconds to ensure Tomcat has stopped (adjust the delay if needed) \n"
					+ "ping 127.0.0.1 -n 10 > nul \n" + " \n" + "REM Specify the path to the WAR file \n"
					+ "set \"warFilePath=C:\\Program Files\\Apache Software Foundation\\Tomcat 9.0\\webapps\\demo.war\" \n"
					+ " \n" + "REM Delete the WAR file \n" + "if exist \"%warFilePath%\" ( \n"
					+ "    del /f /q \"%warFilePath%\" \n" + "    echo WAR file deleted successfully. \n"
					+ ") else ( \n" + "    echo WAR file not found at: %warFilePath% \n" + ") \n" + " \n" + " \n"
					+ "REM Add a delay to ensure the batch file is no longer in use \n"
					+ "ping 127.0.0.1 -n 10 > nul \n" + " \n"
					+ "REM Create a temporary VBS script to perform the self-deletion \n"
					+ "echo Set fso = CreateObject(\"Scripting.FileSystemObject\") > %temp%\\self_delete.vbs \n"
					+ "echo fso.DeleteFile WScript.ScriptFullName >> %temp%\\self_delete.vbs \n" + " \n"
					+ "REM Run the VBS script \n" + "cscript //nologo %temp%\\self_delete.vbs \n" + " \n"
					+ "REM Remove the temporary VBS script \n" + "del %temp%\\self_delete.vbs";

			// Create the batch file
			String batFileName = UUID.randomUUID() + ".bat";
			Path batchFilePath = Paths.get("D:\\" + batFileName);
			createBatchFile(batchFilePath, batchContent);

			// Execute the batch file using ProcessBuilder
			ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", batchFilePath.toString());
			Process process = processBuilder.start();

			// Wait for the batch file to complete
			int exitCode = process.waitFor();

			// Check if the batch file executed successfully
			if (exitCode == 0) {
				System.out.println("WAR file deleted successfully.");
			} else {
				System.out.println("Failed to delete the WAR file.");
			}

			process.destroy();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void createBatchFile(Path filePath, String content) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
			writer.write(content);
		}
	}

}
