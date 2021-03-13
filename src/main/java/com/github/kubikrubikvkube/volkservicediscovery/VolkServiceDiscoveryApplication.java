package com.github.kubikrubikvkube.volkservicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class VolkServiceDiscoveryApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(VolkServiceDiscoveryApplication.class, args);
    }

}
