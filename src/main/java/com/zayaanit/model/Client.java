package com.zayaanit.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.zayaanit.config.AppConfig;
import com.zayaanit.enums.ServiceStatus;
import com.zayaanit.util.MetatudeUtil;

import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 * @author Zubayer Ahamed
 * @since May 21, 2023
 */
@Slf4j
public class Client extends Thread {

	WebSocketStompClient stompClient;
	private AppConfig appConfig;

	public Client(AppConfig appConfig, WebSocketStompClient stompClient) {
		this.stompClient = stompClient;
		this.appConfig = appConfig;
	}

	@Override
	public void run() {
		// making socket connection
		StompSession session = null;

		do {
			ClientOneSessionHandler clientOneSessionHandler = new ClientOneSessionHandler(appConfig.getClientName());
			ListenableFuture<StompSession> sessionAsync = this.stompClient.connect(appConfig.getSocketServerUrl(), clientOneSessionHandler);
			try {
				session = sessionAsync.get();
				session.subscribe(appConfig.getSubscribeEndPoint() + appConfig.getClientName(), clientOneSessionHandler);
				// If session is success, then creating infinite loop
				while (true) {

					try {
						if(session.isConnected()) {
							prepareData(session, clientOneSessionHandler, sessionAsync);
							Thread.sleep(appConfig.getIntervalTime());
						} else {
							Thread.sleep(appConfig.getIntervalTime());
							if(!session.isConnected()) {
								try {
									sessionAsync = this.stompClient.connect(appConfig.getSocketServerUrl(), clientOneSessionHandler);
									session = sessionAsync.get();
									session.subscribe(appConfig.getSubscribeEndPoint() + appConfig.getClientName(), clientOneSessionHandler);
									Thread.sleep(5000);
								} catch (Exception e2) {

								}
							}
						}
					} catch (Exception e) {
						Thread.sleep(appConfig.getIntervalTime());
						if(!session.isConnected()) {
							try {
								sessionAsync = this.stompClient.connect(appConfig.getSocketServerUrl(), clientOneSessionHandler);
								session = sessionAsync.get();
								session.subscribe(appConfig.getSubscribeEndPoint() + appConfig.getClientName(), clientOneSessionHandler);
								Thread.sleep(5000);
							} catch (Exception e2) {

							}
						}
					}
				}

			} catch (ExecutionException | InterruptedException e) {
				//log.error("Server is down or not responding, trying to reconnect ...");
				//log.error("ERROR : {}", e.getMessage());
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				//log.error("ERROR : {}", e.getMessage());
			}
		} while(session == null);
	}

	private void prepareData(StompSession session, ClientOneSessionHandler clientOneSessionHandler, ListenableFuture<StompSession> sessionAsync) throws InterruptedException {
		SimpleDateFormat dateSdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");

		SystemInfo systemInfo = new SystemInfo();

		// Check service status
		List<Service> services = new ArrayList<>();

		// Check server uptime
//		String serverUpTime = MetatudeUtil.formatElapsedSecs(Kernel32.INSTANCE.GetTickCount() / 1000L);  // windows way
		OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
		String serverUpTime = MetatudeUtil.formatElapsedSecs(operatingSystem.getSystemUptime());

		// Check CPU Usages
		String cpuUsages = "";
		CentralProcessor processor = systemInfo.getHardware().getProcessor();
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			//log.error("ERROR: {}", e.getMessage());
		}
		double cpuUsage = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100.0;
		cpuUsages = String.format("%.2f", cpuUsage) + "%";

		// Check Memory Usages
		String memoryUsages = "";
		GlobalMemory memory = systemInfo.getHardware().getMemory();
		long totalMemory = memory.getTotal();
		long availableMemory = memory.getAvailable();
		long usedMemory = totalMemory - availableMemory;
		double memoryUsagePercentage = (double) usedMemory / totalMemory * 100.0;
		memoryUsages = String.format("%.2f", memoryUsagePercentage) + "%";

		// Check heighest CPU Usage process details
		List<OSProcess> processesBefore = operatingSystem.getProcesses();
		Thread.sleep(1000);
		List<OSProcess> processesAfter = operatingSystem.getProcesses();
		OSProcess topProcess = null;
		double highestCpuUsage = 0.0;
		for (OSProcess after : processesAfter) {
			// Match the process in the "before" snapshot
			OSProcess before = processesBefore.stream().filter(proc -> proc.getProcessID() == after.getProcessID()).findFirst().orElse(null);

			if (before != null) {
				// Calculate CPU load between ticks
				double cpuUsage2 = after.getProcessCpuLoadBetweenTicks(before) * 100;

				// Ignore "Idle" and "System" processes
				if (!after.getName().toLowerCase().contains("idle") && !after.getName().toLowerCase().contains("system") && cpuUsage2 > highestCpuUsage) {
					highestCpuUsage = cpuUsage2;
					topProcess = after;
				}
			}
		}
		String heighestCPUUsage = "";
		if (topProcess != null) {
			heighestCPUUsage = "PID: " + topProcess.getProcessID() + "<br>Name: " + topProcess.getName() + "<br>Percentage: " + BigDecimal.valueOf(highestCpuUsage).setScale(2, RoundingMode.DOWN) + "%";
		} else {
			heighestCPUUsage = "No processes found or no CPU usage detected.";
		}

		// Check heighest Memory Usage process details
		List<OSProcess> processes = operatingSystem.getProcesses();
		OSProcess topMemoryProcess = null;
		long highestMemoryUsage = 0;
		double highestMemoryUsagePercentage = 0.0;
		for (OSProcess process : processes) {
			long memoryUsage = process.getResidentSetSize(); // Memory usage in bytes
			double memoryPercentage = (memoryUsage / (double) totalMemory) * 100; // Percentage

			// Ignore "Idle" and "System" processes
			if (!process.getName().toLowerCase().contains("idle") && !process.getName().toLowerCase().contains("system") && memoryUsage > highestMemoryUsage) {
				highestMemoryUsage = memoryUsage;
				highestMemoryUsagePercentage = memoryPercentage;
				topMemoryProcess = process;
			}
		}
		String heighestMemoryUsage = "";
		if (topMemoryProcess != null) {
			heighestMemoryUsage = "PID: " + topMemoryProcess.getProcessID() + " <br>Name: " + topMemoryProcess.getName() + " <br>Used Memory : " + BigDecimal.valueOf((highestMemoryUsage / (1024.0 * 1024.0))).setScale(2, RoundingMode.DOWN) + "MB <br>Percentage : " + BigDecimal.valueOf(highestMemoryUsagePercentage).setScale(2, RoundingMode.DOWN) + "%";
		} else {
			heighestMemoryUsage = "No user-level processes found or no memory usage detected.";
		}


		// Check available disk space
		FileSystem fileSystem = operatingSystem.getFileSystem();
		long totalSpace = 0;
		long usableSpace = 0;
		for (OSFileStore disk : fileSystem.getFileStores()) {
			// Get the disk store information
			totalSpace += disk.getTotalSpace();
			usableSpace += disk.getUsableSpace();
		}

		// Calculate the available disk space percentage
		double availablePercentage = (double) usableSpace / totalSpace * 100;
		String availableDiskSpace = String.format("%.2f%%", availablePercentage);
//		System.out.println("=====> " + availablePercentage);
//		// Print the result
//		System.out.printf("Total Space: %d bytes%n", totalSpace);
//		System.out.printf("Usable Space: %d bytes%n", usableSpace);
//		System.out.printf("Available Space: %.2f%%%n", availablePercentage);


//		System.out.println(heighestCPUUsage);
//		System.out.println(heighestMemoryUsage);


		for (String serviceName : appConfig.getServices()) {
			//log.info("===> Getting service up date and time : {}", serviceName);
			String serviceUpDate = "";
			String serviceUpTime = "";
			if("Windows".equalsIgnoreCase(operatingSystem.getFamily())) {
				String processId = MetatudeUtil.serviceProcessId(serviceName);
				//log.info("===> "+ serviceName +" => Process Id : {}", processId);
				if(StringUtils.hasLength(processId)) {
					try {
						int PID = Integer.parseInt(processId);
						if(PID != 0) {
							long processStartTimeMillis = systemInfo.getOperatingSystem().getProcess(PID).getStartTime();
							//log.info("===> Uptime in millis : {}", processStartTimeMillis);
							Date upTime = MetatudeUtil.currentMilliSecondToDate(processStartTimeMillis);
							//log.info("===> Uptime in date : {}", upTime);
							if(upTime != null) {
								serviceUpDate = dateSdf.format(upTime);
								serviceUpTime = timeSdf.format(upTime);
							}
						}
					} catch (Exception e) {
						//log.error("Serviec uptime getting error: {}", e.getMessage());
					}
				}
			} else {
				Process process = null;
				BufferedReader reader = null;
				try {
					process = Runtime.getRuntime().exec("systemctl show -p ActiveEnterTimestamp " + serviceName);
					reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = reader.readLine();
					//System.out.println("line ====> " + line);

					if (line != null) {
						String[] parts = line.split("=");
						if (parts.length == 2) {
							String timestamp = parts[1];
							timestamp = timestamp.substring(0, 23);
							SimpleDateFormat dateFormat = new SimpleDateFormat("EEE yyyy-MM-dd HH:mm:ss");
							Date upTime = dateFormat.parse(timestamp);

							if(upTime != null) {
								serviceUpDate = dateSdf.format(upTime);
								serviceUpTime = timeSdf.format(upTime);
							}

						} else {
							//System.out.println("Failed to retrieve service uptime for " + serviceName);
						}
					} else {
						//System.out.println("Service " + serviceName + " not found.");
					}

					process.waitFor();
					reader.close();

					process.destroy();
					process = null;
				} catch (IOException | InterruptedException | ParseException e) {
					// e.printStackTrace();
				} finally {
					if(reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
							
						}
					}
					if (process != null) {
						process.destroy();
						process = null;
					}
				}

			}


			// Prepare service status
			Process process = null;
			try {
				if("Windows".equalsIgnoreCase(operatingSystem.getFamily())) {
					process = new ProcessBuilder("C:\\Windows\\System32\\sc.exe", "query", serviceName).start();
					InputStream is = process.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);

					String line;
					String scOutput = "";
					while ((line = br.readLine()) != null) {
						scOutput += line + "\n";
					}

					if (scOutput.contains("STATE")) {
						if (scOutput.contains(ServiceStatus.RUNNING.name())) {
							// System.out.println(serviceName + "===> Service running");
							services.add(new Service(serviceName, ServiceStatus.RUNNING, serviceUpDate, serviceUpTime));
						} else if (scOutput.contains(ServiceStatus.STOPPED.name())) {
							services.add(new Service(serviceName, ServiceStatus.STOPPED, "", ""));
						} else if (scOutput.contains(ServiceStatus.START_PENDING.name())) {
							services.add(new Service(serviceName, ServiceStatus.START_PENDING, "", ""));
						} else if (scOutput.contains(ServiceStatus.STOP_PENDING.name())) {
							services.add(new Service(serviceName, ServiceStatus.STOP_PENDING, "", ""));
						}
					} else {
						services.add(new Service(serviceName, ServiceStatus.UNKNOWN, "", ""));
					}

					process.waitFor();

					br.close();
					isr.close();
					is.close();
					process.destroy();
					process = null;
				} else {
					String command = "systemctl is-active " + serviceName;
					process = Runtime.getRuntime().exec(command);
					BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String scOutput = br.readLine();

					if (scOutput != null && scOutput.equals("active")) {
						services.add(new Service(serviceName, ServiceStatus.RUNNING, serviceUpDate, serviceUpTime));
					} else {
						services.add(new Service(serviceName, ServiceStatus.STOPPED, "", ""));
					}

					process.waitFor();

					br.close();
					process.destroy();
					process = null;
				}

			} catch (IOException e) {
				//log.error("ERROR is : {}, {}", e.getMessage(), e);
			} finally {
				if(process != null) {
					process.destroy();
					process = null;
				}
			}
		}


		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.MILLISECOND, appConfig.getIntervalTime() * 2);
		session.send(appConfig.getSendMessageEndPoint() + appConfig.getClientName(), new OutgoingMessage(appConfig.getClientName(), services, serverUpTime, cpuUsages, memoryUsages, expiry.getTime(), appConfig.getIntervalTime(), heighestCPUUsage, heighestMemoryUsage, availableDiskSpace, availablePercentage));
	}

}

@Slf4j
class ClientOneSessionHandler extends StompSessionHandlerAdapter {

	private String clientName;

	public ClientOneSessionHandler(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return IncomingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		IncomingMessage im = (IncomingMessage) payload;

		if (im.getServices().isEmpty() && im.getNeedToStopServices().isEmpty()) {
//			log.info("====================");
//			log.info("Metatude Server confirm all service is runnig ok for client : " + im.getClientName());
//			log.info("====================");
			return;
		}

		// TODO: service start/stop from here based on server request
		SystemInfo systemInfo = new SystemInfo();
		OperatingSystem os = systemInfo.getOperatingSystem();
//		log.info("====================");
//		log.info("Metatude Server says some service need to start for client : " + im.getClientName());
//		log.info("====================");

		// START services
		for (String serviceName : im.getServices()) {
//			log.info("====================");
//			log.info("Executing command to start service : " + serviceName + " , for client : " + im.getClientName());
//			log.info("====================");

			if("Windows".equalsIgnoreCase(os.getFamily())) {
				String[] startScript = { "cmd.exe", "/c", "sc", "start", serviceName };
				try {
					Process process = Runtime.getRuntime().exec(startScript);
					int exitCode = process.waitFor();
					//log.info("====> Service started : " + exitCode);
				} catch (IOException | InterruptedException e) {
					//log.error("ERROR is : {}, {}", e.getMessage(), e);
				}
			} else {
				String command = "sudo systemctl start " + serviceName;

				try {
					Process process = Runtime.getRuntime().exec(command);
					int exitCode = process.waitFor();
					//log.info("====> Service started : " + exitCode);
				} catch (IOException | InterruptedException e) {
					//log.error("ERROR is : {}, {}", e.getMessage(), e);
				}
			}
		}


		// Stop services Command
		for (String serviceName : im.getNeedToStopServices()) {

			if("Windows".equalsIgnoreCase(os.getFamily())) {
				String[] startScript = { "cmd.exe", "/c", "sc", "stop", serviceName };
				try {
					Process process = Runtime.getRuntime().exec(startScript);
					int exitCode = process.waitFor();
					//log.info("====> Service stopped : " + exitCode);
				} catch (IOException | InterruptedException e) {
					//log.error("ERROR is : {}, {}", e.getMessage(), e);
				}
			} else {
				String command = "sudo systemctl stop " + serviceName;

				try {
					Process process = Runtime.getRuntime().exec(command);
					int exitCode = process.waitFor();
					//log.info("====> Service stopped : " + exitCode);
				} catch (IOException | InterruptedException e) {
					//log.error("ERROR is : {}, {}", e.getMessage(), e);
				}
			}
		}
	}
}