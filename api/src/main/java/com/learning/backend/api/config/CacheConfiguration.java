package com.learning.backend.api.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String TASKS_BY_ID_CACHE = "tasksById";

    @Bean
    CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(TASKS_BY_ID_CACHE);
    }
}
