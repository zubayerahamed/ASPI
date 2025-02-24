package com.zayaanit.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
@Service
public class AppConfig {

	@Value("${app.template-path}")
	private String reportTemplatepath;

	@Value("${app.tomcat-service:Tomcat8}")
	private String tomcatServiceName;

	@Value("${app.database-name:demo}")
	private String databaseName;

	@Value("${app.backup-location:/demo/backup}")
	private String backupLocation;

	@Value("${app.version}")
	private String appVersion;

	@Value("${app.audit-enable:false}")
	private boolean isAuditEnable;

	@Value("${app.import-export}")
	private String importExportPath;

	@Value("${client.service.names}")
	private List<String> services;

	@Value("${client.interval}")
	private Integer intervalTime;

	@Value("${client.name}")
	private String clientName;

	@Value("${socket.server.conurl}")
	private String socketServerUrl;

	@Value("${socket.server.subscribe}")
	private String subscribeEndPoint;

	@Value("${socket.server.sendurl}")
	private String sendMessageEndPoint;
}
