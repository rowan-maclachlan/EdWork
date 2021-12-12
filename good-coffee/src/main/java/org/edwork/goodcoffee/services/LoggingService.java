package org.edwork.goodcoffee.services;

public interface LoggingService {
    
    public void info(String log);

    public void err(String log);

    void err(String log, Exception e);
}
