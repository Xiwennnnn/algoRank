package com.algo.crawler.contest;

import cn.hutool.http.HttpConnection;
import cn.hutool.http.HttpUtil;
import com.algo.util.HttpRequester;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;

import static org.junit.jupiter.api.Assertions.*;

class LuoGuCrawlerTest {
    private static final String URL = "https://www.luogu.com.cn/contest/list?page=1&_contentOnly=1";

    @Test
    public void test() throws IOException {
        System.out.println(HttpRequester.get(URL));
    }

}