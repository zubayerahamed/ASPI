package com.zayaanit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.zayaanit.config.AppConfig;
import com.zayaanit.model.Client;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since May 21, 2023
 */
@Slf4j
@Component
public class ProcessStarter implements CommandLineRunner {

	@Autowired
	private AppConfig appConfig;

	@Override
	public void run(String... args) throws Exception {
//		log.info("====================");
//		log.info("Metatude Client is runnig for client : " + appConfig.getClientName());
//		log.info("====================");

		WebSocketClient client = new StandardWebSocketClient();
		WebSocketStompClient stompClient = new WebSocketStompClient(client);
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());

		new Client(appConfig, stompClient).start();
	}

}
