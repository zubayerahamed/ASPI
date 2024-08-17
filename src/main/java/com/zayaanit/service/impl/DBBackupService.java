package com.zayaanit.service.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 27, 2023
 */
@Slf4j
@Service
public class DBBackupService extends AbstractGenericService {

	public void performBackup(String dbName, String backupFilePath) {
		try {
			String sql = "BACKUP DATABASE "+ dbName +" TO DISK = ? WITH COMPRESSION";
			jdbcTemplate.update(sql, backupFilePath);
			log.info("=====> Backup completed successfully!");
		} catch (Exception e) {
			log.error("Error while performing backup: {}",e.getMessage());
		}
	}
}
