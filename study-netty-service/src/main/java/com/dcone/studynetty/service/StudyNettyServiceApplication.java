package com.dcone.studynetty.service;

import com.dcone.studynetty.service.nettycore.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class StudyNettyServiceApplication implements CommandLineRunner {

    @Resource
    private NettyServer server;

    public static void main(String[] args) {
        SpringApplication.run(StudyNettyServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        server.startServer();
    }
}
