package cn.think.in.java;

import static org.junit.Assert.*;

public class IPUtilTest {

    @org.junit.Test
    public void getLocalIpAddress() {
        System.out.println(IPUtil.getLocalIpAddress());
    }
}