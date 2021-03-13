package com.github.kubikrubikvkube.volkservicediscovery;

import com.github.kubikrubikvkube.volkservicediscovery.util.IPAddressUtils;
import com.github.kubikrubikvkube.volkservicediscovery.util.LocalIpResolver;
import inet.ipaddr.IPAddressString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetAddress;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VolkServiceDiscoveryController {
    private final LocalIpResolver localIpResolver;
    private final IPAddressUtils ipAddressUtils;

    @GetMapping("/info")
    public Mono<Long> get() {
        return localIpResolver.resolve()
                .map(InetAddress::getHostAddress)
                .doOnNext(ip -> log.info("VOLK service discovery localhost IP resolved as '{}'", ip))
                .map(ipAddressUtils::clearLastOctet)
                .map(s -> new IPAddressString(s + "/24"))
                .map(ipAddressUtils.convertIPAddressStringToIPAddress())
                .flatMapMany(ipAddress -> Flux.fromStream(ipAddress.stream()))
                .filter(ipAddressUtils::isReachable)
                .doOnNext(e -> log.info("Reachable host: {}", e))
                .count();
    }
}
