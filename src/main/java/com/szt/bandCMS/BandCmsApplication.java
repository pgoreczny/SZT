package com.szt.bandCMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BandCmsApplication {
    static String IMAGE_DIR;

    public static void main(String[] args) {
        IMAGE_DIR = "/pics/";
        SpringApplication.run(BandCmsApplication.class, args);
    }
}
