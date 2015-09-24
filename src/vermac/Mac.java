/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vermac;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dandres
 */
public class Mac  {
    
     private static String mac;

   
    
    public String leerMac() throws SocketException {
        Pattern p2 = Pattern.compile("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}");
        Pattern p = Pattern.compile("[0-9a-fA-F]{1,2}-[0-9a-fA-F]{1,2}-[0-9a-fA-F]{1,2}-[0-9a-fA-F]{1,2}-[0-9a-fA-F]{1,2}-[0-9a-fA-F]{1,2}");
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        boolean flag = false;

        String tmp;
        //String mac = null;
        mac = null;
        String ip = null;

        fornetwork:
        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.isUp()) {
                Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (!inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
                        byte[] macbytes = netint.getHardwareAddress();
                        if (macbytes == null || macbytes.length == 0) {
                            continue;
                        }
                        for (int i = 0; i < macbytes.length; i++) {
                            tmp = Integer.toHexString(0xFF & macbytes[i]).toUpperCase();
                            mac += tmp + "-";
                        }
                        mac = mac.substring(0, mac.length() - 1);
                        Matcher m = p.matcher(mac);
                        Matcher ipm = p2.matcher(inetAddress.getHostAddress());
                        if (ipm.matches() && m.matches()) {
                            ip = inetAddress.getHostAddress();
                            flag = true;
                            break fornetwork;
                        }
                        mac = "";
                    }
                }
            }
            

        }

        //local
        if (!flag) {
            nets = NetworkInterface.getNetworkInterfaces();
            fornetwork2:
            for (NetworkInterface netint : Collections.list(nets)) {
                if (netint.isUp()) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
                            byte[] macbytes = netint.getHardwareAddress();
                            if (macbytes == null || macbytes.length == 0) {
                                continue;
                            }
                            for (int i = 0; i < macbytes.length; i++) {
                                tmp = Integer.toHexString(0xFF & macbytes[i]).toUpperCase();
                                mac += tmp + "-";
                            }
                            mac = mac.substring(0, mac.length() - 1);
                            Matcher m = p.matcher(mac);
                            Matcher ipm = p2.matcher(inetAddress.getHostAddress());
                            if (ipm.matches() && m.matches()) {
                                ip = inetAddress.getHostAddress();
                                flag = true;
                                break fornetwork2;
                            }
                            mac = "";
                        }
                    }
                }
            }
        }
        System.out.println("******************************************");
        System.out.println("IP de la maquina: " + ip);
        System.out.println("MAC Address: " + mac);
        System.out.println("******************************************");

        return mac;
    }

    public static String getMac() {
        return mac;
    }

    public static void setMac(String mac) {
        Mac.mac = mac;
    }
    
}
