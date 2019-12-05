package com.yellowdoge.app.xiyoumusic.service;

import com.yellowdoge.app.xiyoumusic.model.HuiFu;
import com.yellowdoge.app.xiyoumusic.model.PingLun;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

public interface MusicService {
    MyResponse<?> getMusicInfo(long gqid, String xh);
    MyResponse<?> shoucang(long gqid, String xh);
    MyResponse<?> getChoucang(String xh);
    MyResponse<?> pinglun(PingLun pingLun);
    MyResponse<?> huifu(HuiFu huiFu);
    MyResponse<?> getPinglun(long gqid);
    ResponseEntity<InputStreamResource> download(long gqid);
    MyResponse<?> deletePinglun(long plid);
    MyResponse<?> deleteHuifu(long hfid);
    MyResponse<?> getMyMusic(String xh);
    MyResponse<?> getDongtai(String xh, int startPage);
    MyResponse<?> getHuifu(String xh, int startPage);
}
