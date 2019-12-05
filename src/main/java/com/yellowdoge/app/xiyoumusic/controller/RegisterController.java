package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.service.UserService;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/register/{xh}/{mm}")
    public MyResponse<?> register(@PathVariable(name = "xh") String xh , @PathVariable(name = "mm") String mm){
        MyResponse<?> myResponse = userService.register(xh , mm);
        return myResponse;
    }
}
