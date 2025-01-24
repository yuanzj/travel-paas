package com.jxmk.device.cabinet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ScheduleConfig {
    
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(1,
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "schedule-" + threadNumber.getAndIncrement());
                        t.setDaemon(true);
                        return t;
                    }
                });
    }
} 