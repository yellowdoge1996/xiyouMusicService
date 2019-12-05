package com.yellowdoge.app.xiyoumusic.service;

import com.yellowdoge.app.xiyoumusic.model.Music;
import com.yellowdoge.app.xiyoumusic.model.User;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    MyResponse<?> register(String xh , String mm);
    MyResponse<?> login(String xh , String mm);
    MyResponse<?> login(String uuid);
    MyResponse<?> modify(User user);
    MyResponse<?> uploadTx(String xh , MultipartFile file);
    MyResponse<?> logout(String xh, String uuid);
    MyResponse<?> uploadMusic(Music music, MultipartFile[] files);
    MyResponse<?> isGuanzhu(String gzzxh, String bgzzxh);
    MyResponse<?> guanzhu(String gzzxh, String bgzzxh);
    MyResponse<?> getGuanzhu(String xh, String mode);
}
