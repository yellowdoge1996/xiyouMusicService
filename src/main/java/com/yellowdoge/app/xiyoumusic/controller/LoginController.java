package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.service.UserService;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login/{xh}/{mm}")
    public MyResponse<?> login(@PathVariable(name = "xh") String xh , @PathVariable(name = "mm") String mm){
        MyResponse<?> myResponse = userService.login(xh , mm);
        System.out.println(xh+mm);
        return myResponse;
    }

    @GetMapping(value = "/loginByUUID/{uuid}")
    public MyResponse<?> loginByUUID(@PathVariable(name = "uuid") String uuid){
        MyResponse<?> myResponse = userService.login(uuid);
        System.out.println(uuid);
        return myResponse;
    }
}
