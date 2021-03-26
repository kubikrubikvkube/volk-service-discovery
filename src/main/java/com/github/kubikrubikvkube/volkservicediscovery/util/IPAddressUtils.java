package com.github.kubikrubikvkube.volkservicediscovery.util;

import com.github.kubikrubikvkube.volkservicediscovery.exception.VolkRuntimeException;
import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.function.Function;

@Slf4j
@Component
public class IPAddressUtils {

    /**
     * Timeout when testing if host is reachable
     */
    @Value("${discovery.timeout.isReachable.ms}")
    private int isReachableTimeout;

    /**
     * Clears last octet.
     * For example: 192.168.1.105 becomes 192.168.1.0
     */
    public String clearLastOctet(String hostAddress) {
        String dot = "\\.";
        String[] split = hostAddress.split(dot);
        if (split.length != 0) {
            int lastOctet = split.length - 1;
            split[lastOctet] = "0";

            return String.join(".", split);
        }
        return null;
    }

    /**
     * Tests if {@link IPAddress} is reachable within {@link IPAddressUtils#isReachableTimeout}
     */
    public boolean isReachable(IPAddress ipAddress) {
        String ip = ipAddress.toInetAddress().getHostAddress();
        try {
            return InetAddress.getByName(ip).isReachable(isReachableTimeout);
        } catch (IOException e) {
            log.debug("IPAddress '{}' is not reachable within {} ms", ip, isReachableTimeout);
        }
        return false;
    }

    public Function<IPAddressString, IPAddress> convertIPAddressStringToIPAddress() {
        return ipAddressString -> {
            try {
                return ipAddressString.toAddress();
            } catch (AddressStringException addressStringException) {
                throw new VolkRuntimeException(addressStringException);
            }
        };
    }
}
