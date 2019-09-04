package com.sicredi.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;

@SpringBootApplication
@EnableCaching
public class AppApplication {

    @Value("${queue.schedule.name}")
    private String scheduleQueue;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }


    @Bean
    public Queue queue() {
        return new Queue(scheduleQueue, true);
    }
}
