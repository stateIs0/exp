package cn.think.in.java;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


@Slf4j
public class IPUtil {

    public static String getLocalIpAddress() {
        StringBuffer ips = new StringBuffer();
        Enumeration<NetworkInterface> allNetInterfaces; // 定义网络接口枚举类
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {

                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface
                        .getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if ((ip instanceof Inet4Address)
                            && !"127.0.0.1".equals(ip.getHostAddress())) {
                        ips.append(ip.getHostAddress()).append("|");
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取本地ip异常", e);
        }

        return ips.substring(0, ips.toString().length() - 1);
    }

}
