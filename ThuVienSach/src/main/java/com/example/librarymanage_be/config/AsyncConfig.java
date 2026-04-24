package com.example.librarymanage_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "statsExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   //mặc định 5 thread
        executor.setMaxPoolSize(10);   //max: 10
        executor.setQueueCapacity(50); //hàng chờ: 50 task
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
