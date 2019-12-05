package com.yellowdoge.app.xiyoumusic.service.impl;

import com.github.pagehelper.PageHelper;
import com.yellowdoge.app.xiyoumusic.mapper.*;
import com.yellowdoge.app.xiyoumusic.model.*;
import com.yellowdoge.app.xiyoumusic.service.MusicService;
import com.yellowdoge.app.xiyoumusic.util.FileTools;
import com.yellowdoge.app.xiyoumusic.util.MyException;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import com.yellowdoge.app.xiyoumusic.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "musicService")
public class MusicServiceImpl implements MusicService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MusicMapper musicMapper;
    @Autowired
    ShouCangMapper shouCangMapper;
    @Autowired
    PingLunMapper pingLunMapper;
    @Autowired
    HuiFuMapper huiFuMapper;

    @Override
    public MyResponse<?> getMusicInfo(long gqid, String xh) {
        try{
            boolean isShoucang = false;
            Music music = musicMapper.selectByPrimaryKey(gqid);
            if (music == null){
                throw new MyException("歌曲不存在");
            }
            User user = userMapper.selectByPrimaryKey(music.getZzxh());
            if (user == null){
                throw new MyException("作者学号不存在");
            }
            User o = userMapper.selectByPrimaryKey(xh);
            if (o != null){
                ShouCangKey shouCangKey = new ShouCangKey();
                shouCangKey.setGqid(gqid);
                shouCangKey.setXh(xh);
                ShouCang shouCang = shouCangMapper.selectByPrimaryKey(shouCangKey);
                if (shouCang != null){
                    isShoucang = true;
                }
            }
            String filepath = FileTools.getJarRootPath()+ File.separator+music.getZzxh()+File.separator
                    +music.getGqid()+File.separator;
            String nc = user.getNc();
            int filelenth = FileTools.getMP3Lenth(filepath);
            if (filelenth == 0){
                throw new MyException("获取歌曲文件大小异常");
            }
            long filetime = FileTools.getMP3Time(filepath);
            if (filetime == 0 || filetime == -1){
                throw new MyException("获取歌曲时长异常");
            }
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg(nc+","+filetime+","+filelenth+","+isShoucang);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> shoucang(long gqid, String xh) {
        try{
            Music music = musicMapper.selectByPrimaryKey(gqid);
            if (music == null){
                throw new MyException("歌曲不存在");
            }
            User user = userMapper.selectByPrimaryKey(xh);
            if (user != null){
                ShouCangKey shouCangKey = new ShouCangKey();
                shouCangKey.setGqid(gqid);
                shouCangKey.setXh(xh);
                ShouCang shouCang = shouCangMapper.selectByPrimaryKey(shouCangKey);
                if (shouCang != null){
                    int i = shouCangMapper.deleteByPrimaryKey(shouCangKey);
                    if (i == 0){
                        throw new MyException("取消收藏失败");
                    }
                }else{
                    shouCang = new ShouCang();
                    shouCang.setGqid(gqid);
                    shouCang.setXh(xh);
                    shouCang.setScsj(SnowFlake.nextId());
                    int insert = shouCangMapper.insert(shouCang);
                    if (insert == 0){
                        throw new MyException("收藏失败");
                    }
                }
            }else {
                throw new MyException("请先登录");
            }
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getChoucang(String xh) {
        try{
            List<Music> musicList = new ArrayList<>();
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }else {
                List<ShouCang> shouCangs = shouCangMapper.getShoucang(xh);
                for (ShouCang shouCang : shouCangs) {
                    Music music = musicMapper.selectByPrimaryKey(shouCang.getGqid());
                    if (music != null){
                        musicList.add(music);
                    }
                }
            }
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            if (musicList.size() != 0) {
                myResponse.setMusicData(musicList);
            }
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> pinglun(PingLun pingLun) {
        try{
            if (pingLun == null){
                throw new MyException("非法请求");
            }
            if (pingLun.getPlnr() == null || pingLun.getPlnr().equals("")){
                throw new MyException("请输入评论内容");
            }
            User user = userMapper.selectByPrimaryKey(pingLun.getPlzxh());
            if (user == null) {
                throw new MyException("请先登录");
            }
            Music music = musicMapper.selectByPrimaryKey(pingLun.getGqid());
            if (music == null){
                throw new MyException("歌曲不存在");
            }
            pingLun.setPlznc(user.getNc());
            pingLun.setPlid(SnowFlake.nextId());
            int insert = pingLunMapper.insert(pingLun);
            if (insert == 0){
                throw new MyException("评论失败");
            }
            List<CommentBean> commentBeanList = pingLunMapper.getCommentBean(pingLun.getPlid());
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setCommentData(commentBeanList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> huifu(HuiFu huiFu) {
        try{
            User user = userMapper.selectByPrimaryKey(huiFu.getHfzxh());
            if (user == null){
                throw new MyException("请先登录");
            }
            User buser = userMapper.selectByPrimaryKey(huiFu.getBhfzxh());
            if (buser == null){
                throw new MyException("回复的用户不存在");
            }
            PingLun pingLun = pingLunMapper.selectByPrimaryKey(huiFu.getPlid());
            if (pingLun == null){
                throw new MyException("评论已删除");
            }
            huiFu.setHfid(SnowFlake.nextId());
            int i = huiFuMapper.insertSelective(huiFu);
            if (i == 0){
                throw new MyException("回复失败");
            }
            List<CommentBean> commentBeanList = pingLunMapper.getCommentBean(huiFu.getPlid());
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setCommentData(commentBeanList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getPinglun(long gqid) {
        try{
            Music music = musicMapper.selectByPrimaryKey(gqid);
            if (music == null){
                throw new MyException("歌曲不存在");
            }
            List<CommentBean> commentBeanList = pingLunMapper.getComment(gqid);
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setCommentData(commentBeanList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> download(long gqid) {
        Music music = musicMapper.selectByPrimaryKey(gqid);
        if (music == null){
            return null;
        }
        String dir = FileTools.getJarRootPath()+ File.separator+music.getZzxh()+File.separator
                +gqid+File.separator;
        File musicfile = FileTools.getMp3File(dir);
        if (musicfile == null){
            return null;
        }
        FileSystemResource fileSystemResource = new FileSystemResource(musicfile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileSystemResource.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        try {
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileSystemResource.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(fileSystemResource.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MyResponse<?> deletePinglun(long plid) {
        try{
            PingLun pingLun = pingLunMapper.selectByPrimaryKey(plid);
            if (pingLun == null){
                throw new MyException("评论不存在");
            }
            int i = pingLunMapper.deleteByPrimaryKey(plid);
            if (i == 0){
                throw new MyException("评论删除失败");
            }
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> deleteHuifu(long hfid) {
        try{
            HuiFu huiFu = huiFuMapper.selectByPrimaryKey(hfid);
            if (huiFu == null){
                throw new MyException("回复不存在");
            }
            int i = huiFuMapper.deleteByPrimaryKey(hfid);
            if (i == 0){
                throw new MyException("回复删除失败");
            }
            List<CommentBean> commentBeanList = pingLunMapper.getCommentBean(huiFu.getPlid());
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setCommentData(commentBeanList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getMyMusic(String xh) {
        try{
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }
            List<Music> musicList = musicMapper.myMusic(xh);
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            if (musicList != null){
                myResponse.setMusicData(musicList);
            }
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getDongtai(String xh, int startPage) {
        try{
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }
            PageHelper.startPage(startPage, 5);
            List<Dongtai> dongtaiList = musicMapper.getDongtai(xh);
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            if (dongtaiList != null){
                myResponse.setDongtaiData(dongtaiList);
            }
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getHuifu(String xh, int startPage) {
        try{
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }
            PageHelper.startPage(startPage, 5);
            List<HuifuDetails> huifuDetailsList = huiFuMapper.getHuifu(xh);
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            if (huifuDetailsList != null){
                myResponse.setHuifuData(huifuDetailsList);
            }
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }
}
