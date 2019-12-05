package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.model.HuiFu;
import com.yellowdoge.app.xiyoumusic.model.PingLun;
import com.yellowdoge.app.xiyoumusic.service.MusicService;
import com.yellowdoge.app.xiyoumusic.service.UserService;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class MusicController {
    @Autowired
    MusicService musicService;
    @Autowired
    UserService userService;
    @GetMapping(value = "/musicInfo/{gqid}/{xh}")
    public MyResponse<?> getMusicInfo(@PathVariable(name = "gqid") long gqid, @PathVariable(name = "xh")String xh){
        return musicService.getMusicInfo(gqid, xh);
    }

    @PostMapping(value = "/shoucang/{gqid}/{xh}")
    public MyResponse<?> shoucang(@PathVariable(name = "gqid")long gqid, @PathVariable(name = "xh")String xh){
        return musicService.shoucang(gqid, xh);
    }

    @GetMapping(value = "/getShoucang/{xh}")
    public MyResponse<?> getShoucang(@PathVariable(name = "xh")String xh){
        return musicService.getChoucang(xh);
    }

    @PostMapping(value = "/pinglun")
    public MyResponse<?> pinglun(@RequestBody PingLun pingLun){
        return musicService.pinglun(pingLun);
    }

    @PostMapping(value = "/huifu")
    public MyResponse<?> huifu(@RequestBody HuiFu huiFu){
        return musicService.huifu(huiFu);
    }

    @GetMapping(value = "/download/{gqid}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(name = "gqid")long gqid) throws IOException {
        return musicService.download(gqid);
    }

    @GetMapping(value = "/getPinglun/{gqid}")
    public MyResponse<?> getPinglun(@PathVariable(name = "gqid")long gqid){
        return musicService.getPinglun(gqid);
    }

    @DeleteMapping(value = "/deletePinglun/{plid}")
    public MyResponse<?> deletePinglun(@PathVariable(name = "plid")long plid){
        return musicService.deletePinglun(plid);
    }

    @DeleteMapping(value = "/deleteHuifu/{hfid}")
    public MyResponse<?> deleteHuifu(@PathVariable(name = "hfid")long hfid){
        return musicService.deleteHuifu(hfid);
    }

    @GetMapping(value = "/myMusic/{xh}")
    public MyResponse<?> myMusic(@PathVariable(name = "xh")String xh){
        return musicService.getMyMusic(xh);
    }

    @GetMapping(value = "dongtai/{xh}/{startPage}")
    public MyResponse<?> dongtai(@PathVariable(name = "xh")String xh, @PathVariable(name = "startPage")int startPage){
        return musicService.getDongtai(xh, startPage);
    }

    @GetMapping(value = "/getHuifu/{xh}/{startPage}")
    public MyResponse<?> getHuifu(@PathVariable(name = "xh")String xh, @PathVariable(name = "startPage")int startPage){
        return musicService.getHuifu(xh, startPage);
    }
}
