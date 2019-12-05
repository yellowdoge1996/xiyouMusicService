package com.yellowdoge.app.xiyoumusic.config;

import com.yellowdoge.app.xiyoumusic.util.FileTools;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:///";
        String file = FileTools.getJarRootPath().replace(File.separator,"/")+"/";
        System.out.println(location+file);
        System.out.println("\n\n\n\n");
        registry.addResourceHandler("/tx/**").addResourceLocations(location+file);
        registry.addResourceHandler("/music/**").addResourceLocations(location+file);
    }
}
