package com.sun.lib.net.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 网络请求工具类
 */
public class SunRequestUtil {
    private static SunRequestUtil instance = null;

    private SunRequestUtil() {
    }

    public static SunRequestUtil get() {
        if (instance == null) {
            synchronized (SunRequestUtil.class) {
                if (instance == null) {
                    instance = new SunRequestUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取BaseUrl
     * @param url 需要解析的请求地址
     * @return BaseUrl
     */
    public static String getBaseUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }

    /**
     * 获取 encode 后 Header 值
     * 备注: OkHttp Header 中的 value 不支持 null, \n 和 中文 等特殊字符
     * 后台解析中文 Header 值需要decode（这个后台处理，前端不用理会）
     * @param value encode值
     * @return Header 值
     */
    public static Object getHeaderValueEncoded(Object value) {
        if (value == null) return "null";
        if (value instanceof String) {
            String strValue = ((String) value).replace("\n", "");//换行符
            for (int i = 0, length = strValue.length(); i < length; i++) {
                char c = strValue.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    try {
                        return URLEncoder.encode(strValue, "UTF-8");//中文处理
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        return "";
                    }
                }
            }
            return strValue;
        } else {
            return value;
        }
    }
}
