package com.student;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
class MybatisPlusApplicationTests {
//    @Autowired
//    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
//        BoundZSetOperations test = redisTemplate.boundZSetOps("test");
//        test.add("a",7);
//        test.add("b",9);
//        test.add("c",8);
//        test.rangeByScore(7,8).forEach(item-> System.out.println(item));
    }

}
