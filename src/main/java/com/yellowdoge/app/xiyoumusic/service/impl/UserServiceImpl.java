package com.yellowdoge.app.xiyoumusic.service.impl;

import com.yellowdoge.app.xiyoumusic.mapper.GuanZhuMapper;
import com.yellowdoge.app.xiyoumusic.mapper.MusicMapper;
import com.yellowdoge.app.xiyoumusic.mapper.UserMapper;
import com.yellowdoge.app.xiyoumusic.model.GuanZhu;
import com.yellowdoge.app.xiyoumusic.model.GuanZhuKey;
import com.yellowdoge.app.xiyoumusic.model.Music;
import com.yellowdoge.app.xiyoumusic.model.User;
import com.yellowdoge.app.xiyoumusic.service.UserService;
import com.yellowdoge.app.xiyoumusic.util.FileTools;
import com.yellowdoge.app.xiyoumusic.util.MyException;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import com.yellowdoge.app.xiyoumusic.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private GuanZhuMapper guanZhuMapper;

    @Override
    public MyResponse<?> register(String xh, String mm) {
        try{
            User user = userMapper.selectByPrimaryKey(xh);
            if (user != null){
                throw new MyException("用户已存在");
            }else{
                user = new User(xh, mm);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                user.setSr(sdf.format(new Date()));
                if (userMapper.insertSelective(user)==0){
                    throw new MyException("注册失败，请重新注册");
                }
                else{
                    return login(xh , mm);
                }
            }
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> login(String xh, String mm) {
        try {
            User user = userMapper.selectByPrimaryKey(xh);
            if (user!=null){
                if (user.getMm().equals(mm)){
                    String uuid = UUID.randomUUID().toString();
                    user.setUuid(uuid);
                    userMapper.updateByPrimaryKey(user);
                    user = userMapper.selectByUUID(user.getUuid());

                    List<User> userList = new ArrayList<>();
                    userList.add(user);

                    MyResponse<List> myResponse= new MyResponse<>();
                    myResponse.setState("1");
                    myResponse.setMsg("success");
                    myResponse.setUserData(userList);
                    return myResponse;
                }else{
                    throw new MyException("密码错误");
                }
            }else{
                throw new MyException("用户"+xh+"不存在");
            }
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> logout(String xh, String uuid) {
        try {
            User user = userMapper.selectByPrimaryKey(xh);
            if (user!=null){
                if (uuid.equals(user.getUuid())){
                    user.setUuid(null);
                    if (userMapper.updateByPrimaryKey(user) != 0){
                        MyResponse<List> myResponse= new MyResponse<>();
                        myResponse.setState("1");
                        myResponse.setMsg("success");
                        return myResponse;
                    } else{
                        throw new MyException("清空uuid失败");
                    }
                }else{
                    throw new MyException("登录已过期");
                }
            }else{
                throw new MyException("用户"+xh+"不存在");
            }
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> modify(User user) {
        try {
            userMapper.updateByPrimaryKey(user);
            user = userMapper.selectByUUID(user.getUuid());
            List<User> userList = new ArrayList<>();
            userList.add(user);

            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setUserData(userList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> uploadTx(String xh, MultipartFile file) {
        User user = userMapper.selectByPrimaryKey(xh);
        if (user == null){
            throw new MyException("用户不存在");
        }
        try {
            if (file.isEmpty()){
                throw new MyException("文件为空");
            }
            byte[] bytes = file.getBytes();
            String UPLOADED_FOLDER = FileTools.getJarRootPath()+ File.separator+xh+File.separator;
            File dir = new File(UPLOADED_FOLDER);
            if (!dir.exists()){
                dir.mkdir();
            }
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            System.out.println(path.toAbsolutePath());
            Files.write(path, bytes);
            user.setTxlj("tx/"+xh+"/crop.jpg"+"?time="
                    +System.currentTimeMillis());

            userMapper.updateByPrimaryKey(user);
            user = userMapper.selectByUUID(user.getUuid());
            List<User> userList = new ArrayList<>();
            userList.add(user);

            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setUserData(userList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        } catch (IOException e) {
            e.printStackTrace();
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> uploadMusic(Music music, MultipartFile[] files) {
        User user = userMapper.selectByPrimaryKey(music.getZzxh());
        if (user == null){
            throw new MyException("用户不存在");
        }
        music.setGqid(SnowFlake.nextId());
        try {
            if (files.length == 0){
                throw new MyException("文件为空");
            }
            for (int i = 0; i < files.length; i++) {
                byte[] bytes = files[i].getBytes();
                String UPLOADED_FOLDER = FileTools.getJarRootPath()+ File.separator+music.getZzxh()+File.separator
                        +music.getGqid()+File.separator;
                File dir = new File(UPLOADED_FOLDER);
                if (!dir.exists()){
                    dir.mkdir();
                }
                String filename = files[i].getOriginalFilename();
                if (!filename.endsWith(".jpg")){
                    filename = URLDecoder.decode(filename, "UTF-8");
                    filename = FileTools.StringFilter(filename);
                }
                Path path = Paths.get(UPLOADED_FOLDER + filename);
                System.out.println(path.toAbsolutePath());
                Files.write(path, bytes);
                if (files[i].getOriginalFilename().endsWith(".jpg")){
                    music.setGqct("music/"+music.getZzxh()+"/"+music.getGqid()+"/"+filename
                            +"?time="+System.currentTimeMillis());
                }else{
                    music.setGqlj("music/"+music.getZzxh()+"/"+music.getGqid()+"/"
                            +filename
                            +"?time="+System.currentTimeMillis());
                }
            }
            musicMapper.insert(music);

            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        } catch (IOException e) {
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> isGuanzhu(String gzzxh, String bgzzxh) {
        try {
            GuanZhuKey guanZhuKey = new GuanZhuKey();
            guanZhuKey.setGzzxh(gzzxh);
            guanZhuKey.setBgzzxh(bgzzxh);
            GuanZhu guanZhu = guanZhuMapper.selectByPrimaryKey(guanZhuKey);
            boolean isGuanzhu = guanZhu != null;
            MyResponse<List> myResponse = new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg(isGuanzhu+"");
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> guanzhu(String gzzxh, String bgzzxh) {
        try{
            if (gzzxh.equals(bgzzxh)){
                throw new MyException("不能关注自己");
            }
            User user = userMapper.selectByPrimaryKey(gzzxh);
            if (user == null){
                throw new MyException("请先登录");
            }
            User buser = userMapper.selectByPrimaryKey(bgzzxh);
            boolean isGuanzhu;
            if (buser == null){
                throw new MyException("用户不存在");
            }else {
                GuanZhuKey guanZhuKey = new GuanZhuKey();
                guanZhuKey.setGzzxh(gzzxh);
                guanZhuKey.setBgzzxh(bgzzxh);
                GuanZhu guanZhu = guanZhuMapper.selectByPrimaryKey(guanZhuKey);
                if (guanZhu != null){
                    int i = guanZhuMapper.deleteByPrimaryKey(guanZhuKey);
                    isGuanzhu = false;
                    if (i == 0){
                        throw new MyException("取消关注失败");
                    }
                }else{
                    guanZhu = new GuanZhu();
                    guanZhu.setGzzxh(gzzxh);
                    guanZhu.setBgzzxh(bgzzxh);
                    guanZhu.setGzsj(SnowFlake.nextId());
                    int insert = guanZhuMapper.insert(guanZhu);
                    isGuanzhu = true;
                    if (insert == 0){
                        throw new MyException("关注失败");
                    }
                }
            }
            MyResponse<List> myResponse= new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg(isGuanzhu+"");
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getGuanzhu(String xh, String mode) {
        try {
            User user = userMapper.selectByPrimaryKey(xh);
            if (user == null){
                throw new MyException("请先登录");
            }
            List<User> userList;
            if (mode.equals("GUANZHU")){
            userList = guanZhuMapper.myGuanzhu(xh);
            }else if (mode.equals("FAN")){
                userList = guanZhuMapper.myFans(xh);
            }else{
                userList = new ArrayList<>();
            }
            MyResponse<List> myResponse = new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            myResponse.setUserData(userList);
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> login(String uuid) {
        try {
            User user = userMapper.selectByUUID(uuid);
            if (user != null){
                List<User> userList = new ArrayList<>();
                userList.add(user);

                MyResponse<List> myResponse= new MyResponse<>();
                myResponse.setState("1");
                myResponse.setMsg("success");
                myResponse.setUserData(userList);
                return myResponse;
            }else{
                throw new MyException("登录已过期");
            }
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }
}
