package com.yellowdoge.app.xiyoumusic.service.impl;

import com.github.pagehelper.PageHelper;
import com.yellowdoge.app.xiyoumusic.mapper.GuanZhuMapper;
import com.yellowdoge.app.xiyoumusic.mapper.MusicMapper;
import com.yellowdoge.app.xiyoumusic.mapper.ShouCangMapper;
import com.yellowdoge.app.xiyoumusic.mapper.UserMapper;
import com.yellowdoge.app.xiyoumusic.model.Music;
import com.yellowdoge.app.xiyoumusic.model.User;
import com.yellowdoge.app.xiyoumusic.service.SearchService;
import com.yellowdoge.app.xiyoumusic.util.MyException;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "searchService")
public class SearchServiceImpl implements SearchService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private GuanZhuMapper guanZhuMapper;
    @Autowired
    private ShouCangMapper shouCangMapper;

    @Override
    public MyResponse<?> getSuggestions(String keyword) {
        try {
            PageHelper.startPage(1, 10);
            List<String> userlist = userMapper.getSuggestions(keyword);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < userlist.size(); i++) {
                stringBuilder.append(userlist.get(i));
                if (i != userlist.size() - 1) {
                    stringBuilder.append(",");
                }
            }

            if (userlist.size() < 10) {
                PageHelper.startPage(1, 10 - userlist.size());
                List<String> musiclist = musicMapper.getSuggestions(keyword);
                if (musiclist.size() > 0) {
                    if (userlist.size()>0) {
                        stringBuilder.append(",");
                    }
                    for (int i = 0; i < musiclist.size(); i++) {
                        stringBuilder.append(musiclist.get(i));
                        if (i != musiclist.size() - 1) {
                            stringBuilder.append(",");
                        }
                    }
                }
            }
            if (stringBuilder.toString().isEmpty()) {
                throw new MyException("没有建议");
            }
            MyResponse<List> myResponse = new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg(stringBuilder.toString());
            return myResponse;
        }catch (MyException e){
            MyResponse<?> myResponse= new MyResponse<>();
            myResponse.setState("0");
            myResponse.setError(e.getMessage());
            return myResponse;
        }
    }

    @Override
    public MyResponse<?> getSearchResult(String xh, String keyword) {
        try {
            List<User> userlist;
            List<Music> musiclist;
            userlist = userMapper.searchUser(keyword);
            musiclist = musicMapper.searchMusic(keyword);
            if (userlist.size() == 0 && musiclist.size() == 0){
                throw new MyException("没有搜索到相关信息");
            }

            MyResponse<List> myResponse = new MyResponse<>();
            myResponse.setState("1");
            myResponse.setMsg("success");
            if (userlist.size() != 0){
                myResponse.setUserData(userlist);
            }
            if (musiclist.size() != 0){
                myResponse.setMusicData(musiclist);
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
