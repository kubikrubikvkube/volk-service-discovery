package com.github.kubikrubikvkube.volkservicediscovery.util;

import com.github.kubikrubikvkube.volkservicediscovery.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A component to resolve current IP address of available network interface
 */
@Slf4j
@Component
public class LocalIpResolver {

    public Mono<InetAddress> resolve() {
        InetAddress resolvedAddress;
        try {
            var byteAddress = InetAddress.getLocalHost().getAddress();
            resolvedAddress = Inet4Address.getByAddress(byteAddress);
            log.debug("Resolved own ip address as: {}", resolvedAddress);
            return Mono.just(resolvedAddress);
        } catch (UnknownHostException e) {
            throw new ConfigurationException("Can't resolve own ip address");
        }
    }
}
