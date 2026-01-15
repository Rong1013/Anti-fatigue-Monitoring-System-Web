package com.fatigue.monitoring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 抗疲劳制造监测系统启动类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.fatigue.monitoring.mapper")
@EnableCaching
@EnableScheduling
public class FatigueMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(FatigueMonitoringApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("抗疲劳制造监测系统启动成功！");
        System.out.println("========================================\n");
    }
}
