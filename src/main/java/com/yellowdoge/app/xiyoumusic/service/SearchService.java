package com.yellowdoge.app.xiyoumusic.service;

import com.yellowdoge.app.xiyoumusic.util.MyResponse;

public interface SearchService {
    MyResponse<?> getSuggestions(String keyword);
    MyResponse<?> getSearchResult(String xh, String keyword);
}
