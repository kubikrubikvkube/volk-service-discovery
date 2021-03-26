package com.github.kubikrubikvkube.volkservicediscovery.dockerService.endpoints;

import lombok.Getter;

/**
 * Expected endpoints for VOLK services
 */
@Getter
public enum ExpectedEndpoints {
    INFO("/volk/meta/info"),
    HEALTH("/volk/meta/health"),
    HELP("/volk/meta/help");

    private final String urn;

    ExpectedEndpoints(String urn) {
        this.urn = urn;
    }
}
