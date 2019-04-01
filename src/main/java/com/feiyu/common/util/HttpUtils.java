package com.feiyu.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author feiyu127@gmail.com
 * @date 2018-08-29 18:00
 */
public class HttpUtils {

    /**
     * 将获取的字节流转换为Base64字符串
     *
     * @param url get请求的url
     * @return
     */
    public static String getBase64FromUrl(String url) {
        byte[] responseByte = getByteArrayFromUrl(url);
        if (responseByte != null) {
            return Base64.getEncoder().encodeToString(responseByte);
        }
        return null;
    }

    /**
     * 获取url返回的字节流
     *
     * @param url
     * @return
     */
    public static byte[] getByteArrayFromUrl(String url) {
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);) {

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toByteArray(httpResponse.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过url获取返回的字符串
     *
     * @param url
     * @return
     */
    public static String getStringFromUrl(String url, String defaultCharset) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("user-agent", HttpConstant.getRandomUserAgent());
        try (CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpGet);) {

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity(), defaultCharset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发起post请求，返回字符串结果
     * @param url url
     * @param map 参数
     * @return
     */
    public static String postString(String url, Map<String, String> map) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("user-agent", HttpConstant.getRandomUserAgent());
        handleParam(httpPost, map);
        try (CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);) {
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 发起post请求，返回字符串结果
     * @param url url
     * @param map 参数
     * @return
     */
    public static String getString(String url, Map<String, String> map) {
        URI uri = test(url, map);
        HttpGet httpPost = new HttpGet(uri);
        httpPost.addHeader("user-agent", HttpConstant.getRandomUserAgent());
        try (CloseableHttpResponse httpResponse = HttpClients.createDefault().execute(httpPost);) {
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void handleParam(HttpEntityEnclosingRequestBase baseRequest, Map<String, String> map) {
        if (map != null) {
            List<BasicNameValuePair> collect = map.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            try {
                baseRequest.setEntity(new UrlEncodedFormEntity(collect, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
    private static URI test(String url, Map<String, String> map) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            map.forEach((k, v) -> uriBuilder.addParameter(k, v));
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 下载文件到指定路径
     * @param filePath 下载文件的路径名称
     * @param url 网络地址
     * @param isOverride 是否覆盖原始文件
     */
    public static void downloadFile(String filePath, String url, boolean isOverride) {
        Path path = Paths.get(filePath);
        File file = path.toFile();
        if (!isOverride && file.exists()) {
            return;
        }
        try {
            if (!file.exists()) {
                FileUtils.createNewFile(file);
            }
            byte[] byteArrayFromUrl = getByteArrayFromUrl(url);
            Files.write(path, byteArrayFromUrl, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HttpConstant {
    private static String[] USER_AGENT_ARRAY = {
            "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19",
            "Mozilla/5.0 (Linux; U; Android 4.0.4; en-gb; GT-I9300 Build/IMM76D) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 2.2; en-gb; GT-P1000 Build/FROYO) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36",
            "Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19",
            "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0",
            "Mozilla/5.0 (Android; Mobile; rv:14.0) Gecko/14.0 Firefox/14.0"
    };
    private static AtomicInteger useAgentNum = new AtomicInteger(0);

    public static String getRandomUserAgent() {
        int num = useAgentNum.addAndGet(1);
        return USER_AGENT_ARRAY[num % USER_AGENT_ARRAY.length];
    }
}
