package com.shanzhu.em;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class  BackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(BackendApplication.class, args);

        log.info("=====================The project backend started successfully============================");
    }

}
