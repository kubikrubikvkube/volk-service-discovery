package com.github.kubikrubikvkube.volkservicediscovery.dockerService.endpoints;

import lombok.Data;

/**
 * Discovered VOLK service
 */
@Data
public class DockerVolkService {
    private ServiceInfo serviceInfo;
    private HealthStatus healthStatus;
}
