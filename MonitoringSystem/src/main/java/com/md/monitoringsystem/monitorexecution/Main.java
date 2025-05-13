package com.md.monitoringsystem.monitorexecution;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Main {
//    public static void main(String[] args) throws SQLException, InterruptedException {
//        Monitor monitor1 = new Monitor();
//        monitor1.setId(1);
//        monitor1.setMonitorName("Google Monitor");
//        monitor1.setUrl("https://www.google.com");
//        monitor1.setStatus("UP");
//        monitor1.setInterval("1"); // 1 min
//        monitor1.setActive(true);
//
//        Monitor monitor2 = new Monitor();
//        monitor2.setId(2);
//        monitor2.setMonitorName("GitHub Monitor");
//        monitor2.setUrl("https://www.github.com");
//        monitor2.setStatus("UP");
//        monitor2.setInterval("2"); // 2 min
//        monitor2.setActive(true);
//
//
//        Scheduler sc = new Scheduler();
//        sc.addMonitor(monitor1);
//        sc.addMonitor(monitor2);
//        new Thread(sc).start();
////        long lastTime = new Timestamp(System.currentTimeMillis()).getTime();
////        System.out.println(lastTime);
////        Thread.sleep(6000);
////        long endTIme = new Timestamp(System.currentTimeMillis()).getTime(); // 1 min
////        System.out.println(endTIme);
////        System.out.println((endTIme - lastTime));
//    }
    public static void main(String[] args) {
        System.out.println(isValidPassword("AAAA2"));
    }
    private static boolean isValidPassword(String password){
        boolean containsAlphaNumeric = password.matches("[a-zA-Z0-9]{6,16}");
        boolean containsSymbol = false;
        for(char c : password.toCharArray()){
            if(c>='|' && c<='/' || c>=':' && c<='@' || c>='[' && c<='`' || c>='{' && c<='~'){
                containsSymbol = true;
                break;
            }
        }
        return containsAlphaNumeric && containsSymbol && password.length()>8;
    }
}
