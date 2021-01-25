package com.tjh.cache;

import com.tjh.cache.bean.Employee;
import com.tjh.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class Springboot01CacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate; //操作k-v都是字符串的

    @Autowired
    RedisTemplate redisTemplate; //k-v都是对象

    @Autowired
    RedisTemplate<Object, Employee> empRedisTemplate;

    /**
     * Redis常见的五大数据类型
     * String（字符串）、List（列表）、Set（集合）、Hash（散列）、ZSet（有序集合）
     * 五大数据类型的使用：
     * String：stringRedisTemplate.opsForValue()
     * List：stringRedisTemplate.opsForList()
     * Set：stringRedisTemplate.opsForSet()
     * Hash：stringRedisTemplate.opsForHash()
     * ZSet：stringRedisTemplate.opsForZSet()
     */
    @Test
    public void testStringRedisTemplate() {
        //保存String（字符串）数据
        //stringRedisTemplate.opsForValue().append("msg", "hell");
        String msg = stringRedisTemplate.opsForValue().get("msg");
        System.out.println(msg);

        //保存List（列表）数据
        //stringRedisTemplate.opsForList().leftPush("myList","1");
    }

    /**
     * 测试保存对象
     */
    @Test
    public void testRedisTemplate() {
        Employee employee = employeeMapper.getEmpById(1);
        //1、默认使用jdk序列化机制，将序列化后的数据保存到redis中
        //redisTemplate.opsForValue().set("emp-01", employee);
        /**
         * 2、将数据以json的方式保存
         *      2.1、自己将对象转为json
         *      2.2、redisTemplate默认的序列化规则：改变默认的序列化规则
         */
        empRedisTemplate.opsForValue().set("emp-01", employee);
    }
}
