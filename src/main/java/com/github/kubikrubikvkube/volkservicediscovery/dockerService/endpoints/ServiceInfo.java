package com.github.kubikrubikvkube.volkservicediscovery.dockerService.endpoints;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@RequiredArgsConstructor
public class ServiceInfo {
    private final String name;
    private final long version;
    private final String description;
}
