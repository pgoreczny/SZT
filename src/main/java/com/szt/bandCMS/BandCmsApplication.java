package com.szt.bandCMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BandCmsApplication {
	static String IMAGE_DIR;
	public static void main(String[] args) {
		IMAGE_DIR = "/pics/";
		SpringApplication.run(BandCmsApplication.class, args);
	}
}
