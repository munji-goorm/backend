//package com.munjigoorm.backend.config;
//
//import net.sf.ehcache.config.CacheConfiguration;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.concurrent.ConcurrentMapCache;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.http.HttpClient;
//import java.util.Arrays;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//@EnableCaching
//@Configuration
//public class CacheConfig extends CachingConfigurerSupport {
//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {
//
//            @Override
//            protected Cache createConcurrentMapCache(final String name) {
//                HttpClient CacheBuilder;
//                return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS)
//                        .maximumSize(100).build().asMap(), false);
//            }
//        };
//
//        cacheManager.setCacheNames(Arrays.asList("myOrgCache", "myEmployeeCache"));
//        return cacheManager;
//    }
//}
