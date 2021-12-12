package org.edwork.goodcoffee.services;

public class TempLoggingService implements LoggingService {

    @Override
    public void info(String log) {
        System.out.println(log);
    }

    @Override
    public void err(String log) {
        System.err.println(log);
    }

    @Override
    public void err(String log, Exception e) {
        System.err.println(log);
        System.err.println(e.getMessage());
        e.printStackTrace();
    }
}
