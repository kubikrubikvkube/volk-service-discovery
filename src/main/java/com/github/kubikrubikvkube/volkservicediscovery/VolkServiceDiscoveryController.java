package com.github.kubikrubikvkube.volkservicediscovery;

import com.github.kubikrubikvkube.volkservicediscovery.util.LocalIpResolver;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Function;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VolkServiceDiscoveryController {
    private final LocalIpResolver localIpResolver;

    @GetMapping("/info")
    public Mono<Long> get() {
        return localIpResolver.resolve()
                .map(InetAddress::getHostAddress)
                .doOnNext(ip -> log.info("VOLK service discovery localhost IP resolved as '{}'", ip))
                .map(this::clearLastOctet)
                .map(s -> new IPAddressString(s + "/24"))
                .map(getIpAddressStringIPAddressFunction())
                .flatMapMany(ipAddress -> Flux.fromStream(ipAddress.stream()))
                .filter(this::isReacheable)
                .doOnNext(e -> log.info("Reachable host: {}", e))
                .count();

    }

    private Function<IPAddressString, IPAddress> getIpAddressStringIPAddressFunction() {
        return e -> {
            try {
                return e.toAddress();
            } catch (AddressStringException addressStringException) {
                addressStringException.printStackTrace();
            }
            return null;
        };
    }

    private boolean isReacheable(IPAddress ipAddress) {
        int timeout = 15;
        String s = ipAddress.toInetAddress().getHostAddress();
        try {
            return InetAddress.getByName(s).isReachable(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String clearLastOctet(String hostAddress) {
        String[] split = hostAddress.split("\\.");
        if (split.length != 0) {
            int lastOctet = split.length - 1;
            split[lastOctet] = "0";

            return String.join(".", split);
        }
        return null;
    }
}
