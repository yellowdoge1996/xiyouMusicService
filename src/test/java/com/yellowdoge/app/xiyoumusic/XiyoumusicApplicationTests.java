package com.yellowdoge.app.xiyoumusic;

import com.yellowdoge.app.xiyoumusic.listener.SocketUserSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(value = "club.yellowdoge1996.blog.mapper")
public class XiyoumusicApplicationTests {
    @Autowired
    SocketUserSet socketUserSet;
    @Test
    public void contextLoads() {
        socketUserSet.registerUser("201531060482");
        socketUserSet.registerUser("201531060481");
        socketUserSet.registerUser("201531060485");
        socketUserSet.registerUser("201531060483");
        socketUserSet.registerUser("201531060484");
        socketUserSet.registerUser("201531060487");
        socketUserSet.registerUser("201531060486");
        boolean online = socketUserSet.isOnline("201531060482");
        System.out.println(online);
    }

}
