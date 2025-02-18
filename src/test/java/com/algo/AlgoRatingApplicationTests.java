package com.algo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
class AlgoRatingApplicationTests {

    @Autowired
    ServiceTest serviceTest;

    @WithMockUser(username = "USER")
    @Test
    void contextLoads() throws Exception {
        String res = serviceTest.ok();
        System.out.println(res);
    }
}
