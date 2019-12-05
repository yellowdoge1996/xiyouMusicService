package com.yellowdoge.app.xiyoumusic;

import com.yellowdoge.app.xiyoumusic.util.FileTools;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;

import java.io.File;

public class test {
    public static void main(String[] args) {
//        String str = "$%&^*2134234124[12312]【卫栖梧】(123456)<123456>我爱你.mp3?dasdasd";
////        String regEx="[\\[{【(<].*[\\]}】)>]|[`!@#$%^&*()+=|{}':;,/<>?！￥…（）—【】‘；：”“’。，、？]";
//        String regEx="\\?.*";
//        Pattern pattern   =   Pattern.compile(regEx);
//        Matcher matcher   =   pattern.matcher(str);
//        str = matcher.replaceAll("").trim();
////        str = str.replaceAll("[\\pP\\p{Punct}]","");
//        System.out.println(str);
        String dir = FileTools.getJarRootPath()+ File.separator+"201531060459"+File.separator
                +6380901902458880L+File.separator;
        File musicfile = FileTools.getMp3File(dir);
        if (musicfile == null){
        }
        FileSystemResource fileSystemResource = new FileSystemResource(musicfile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileSystemResource.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
    }
}
