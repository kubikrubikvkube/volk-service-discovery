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

    @Value("${discovery.timeout.isReachable.ms}")
    private int isReachableTimeout;

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
