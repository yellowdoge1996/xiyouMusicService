package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.model.Music;
import com.yellowdoge.app.xiyoumusic.model.User;
import com.yellowdoge.app.xiyoumusic.service.UserService;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PutMapping(value = "/modifyUserInfo")
    public MyResponse<?> modifyUserInfo(@RequestBody User user){
        System.out.println(user.toString());
        System.out.println("modifyUserInfo");
        return userService.modify(user);
    }

    @PostMapping(value = "/uploadTx/{xh}")
    public MyResponse<?> uploadTx(@PathVariable(name = "xh")String xh,@RequestParam("file") MultipartFile file){
        return userService.uploadTx(xh, file);
    }

    @PostMapping(value = "/uploadMusic/{xh}/{name}")
    public MyResponse<?> uploadMusic(@PathVariable(name = "xh") String xh, @PathVariable(name = "name") String name, @RequestParam("file") MultipartFile[] files){
        Music music = new Music();
        music.setZzxh(xh);
        music.setGqmc(name);
        return userService.uploadMusic(music, files);
    }

    @GetMapping(value = "/logout/{xh}/{uuid}")
    public MyResponse<?> logout(@PathVariable(name = "xh") String xh, @PathVariable(name = "uuid") String uuid){
        return userService.logout(xh, uuid);
    }

    @GetMapping(value = "/isGuanzhu/{gzzxh}/{bgzzxh}")
    public MyResponse<?> isGuanzhu(@PathVariable(name = "gzzxh")String gzzxh, @PathVariable(name = "bgzzxh")String bgzzxh){
        return userService.isGuanzhu(gzzxh, bgzzxh);
    }

    @PostMapping(value = "/guanzhu/{gzzxh}/{bgzzxh}")
    public MyResponse<?> guanzhu(@PathVariable(name = "gzzxh")String gzzxh, @PathVariable(name = "bgzzxh")String bgzzxh){
        return userService.guanzhu(gzzxh, bgzzxh);
    }

    @GetMapping(value = "/getGuanzhu/{xh}/{mode}")
    public MyResponse<?> getGuanzhu(@PathVariable(name = "xh")String xh, @PathVariable(name = "mode")String mode){
        return userService.getGuanzhu(xh, mode);
    }
}
