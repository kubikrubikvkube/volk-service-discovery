package com.github.kubikrubikvkube.volkservicediscovery.util;

import com.github.kubikrubikvkube.volkservicediscovery.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * A component to resolve current IP address of available network interface
 */
@Slf4j
@Component
public class LocalIpResolver {

    @Value("${localhost.static.ip}")
    private String localhostStaticIp;

    public Mono<InetAddress> resolve() {
        try {
            if (isNotBlank(localhostStaticIp)) {
                log.info("Localhost has predefined static IP: {}", localhostStaticIp);
                return resolveLocalhostStaticIp();
            }

            log.debug("Localhost doesn't have predefined static IP.");
            InetAddress resolvedAddress;
            var byteAddress = InetAddress.getLocalHost().getAddress();
            resolvedAddress = Inet4Address.getByAddress(byteAddress);
            log.debug("Resolved own ip address as: {}", resolvedAddress);
            return Mono.just(resolvedAddress);
        } catch (UnknownHostException e) {
            throw new ConfigurationException("Can't resolve own ip address");
        }
    }

    private Mono<InetAddress> resolveLocalhostStaticIp() throws UnknownHostException {
        InetAddress inetAddress = Inet4Address.getByName(localhostStaticIp);
        log.debug("Resolved own static ip address as: {}", inetAddress);
        return Mono.justOrEmpty(inetAddress);
    }
}
