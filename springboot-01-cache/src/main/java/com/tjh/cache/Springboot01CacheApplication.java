package com.tjh.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 快速体验缓存
 * 步骤：
 *   1、开启基于注解的缓存 @EnableCaching
 *   2、标注缓存注解
 *         @Cacheable
 *         @CacheEvict
 *         @CachePut
 *  默认使用的是：ConcurrentMapCacheManager
 *  而开发中使用缓存中间件：redis、memcached、echace
 *  整合redis作为缓存：
 *      1、使用docker安装redis
 *      2、引入redis的starter
 *      3、配置redis
 */
@MapperScan("com.tjh.cache.mapper")
@SpringBootApplication
@EnableCaching
public class Springboot01CacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(Springboot01CacheApplication.class, args);
    }

}
