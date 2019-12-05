package com.yellowdoge.app.xiyoumusic.controller;

import com.yellowdoge.app.xiyoumusic.service.SearchService;
import com.yellowdoge.app.xiyoumusic.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping(value = "/getSuggestions/{keyword}")
    public MyResponse<?> register(@PathVariable(name = "keyword") String keyword ){
        MyResponse<?> myResponse = searchService.getSuggestions(keyword);
        return myResponse;
    }

    @GetMapping(value = "/search/{xh}/{keyword}")
    public MyResponse<?> search(@PathVariable(name = "xh")String xh, @PathVariable(name = "keyword")String keyword){
        MyResponse<?> myResponse = searchService.getSearchResult(xh, keyword);
        return myResponse;
    }
}
