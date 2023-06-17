package com.ofss.fc.serverstatus.BPDEnvServerStatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Author - @asmhatre
 */

@SpringBootApplication
public class BpdEnvServerStatusApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { return application.sources(BpdEnvServerStatusApplication.class); }

	public static void main(String[] args) {
		SpringApplication.run(BpdEnvServerStatusApplication.class, args);
	}

}