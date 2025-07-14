package com.zss.common.net.interceptor;


import com.zss.base.util.LL;

import okhttp3.Headers;

/**
 * http request info handle object
 * Created by linxiao on 2017/1/19.
 */
public class HttpInfoEntity {

    /**
     * Http协议
     */
    public String protocol;
    /**
     * 请求方式
     */
    public String method;
    /**
     * 请求地址
     */
    public String url;
    /**
     * 请求耗时
     */
    public long tookMills = 0;

    /*---------------request params----------------*/

    public Headers requestHeaders;

    public String requestContentType;

    public long requestContentLength;

    public String requestBody;

    /*---------------response params----------------*/

    public Headers responseHeaders;

    public int responseCode;

    public String responseMessage;

    public long responseContentLength;

    public String responseBody;


    public void logOut(boolean isPrintBody) {
        logLine("----------------------------");
        logLine("url: " + url);
        logLine("protocol: %s,  method: %s", protocol, method);
        logLine("request took time: %d ms", tookMills);
        logLine("response code: %d,  message: %s", responseCode, responseMessage);
        logLine("----------request-----------");
        for (String headerName : requestHeaders.names()) {
            logLine("Headers: %s -> %s", headerName, requestHeaders.get(headerName));
        }
        logLine("Body:");
        if (isPrintBody) {
            logLine(requestBody);
        }
        logLine("----------response----------");
        for (String headerName : responseHeaders.names()) {
            logLine("Headers: %s -> %s", headerName, responseHeaders.get(headerName));
        }
        logLine("Body:");
        logLine(responseBody);
        logLine("----------------------------");
    }

    private void logLine(String message) {
        if (message == null) {
            LL.i("null");
            return;
        }
        if (message.length() < 4000) {
            LL.i(message);
            return;
        }
        int subStart = 0;
        int subEnd = 4000;
        while (subEnd < message.length()) {
            LL.i(message.substring(subStart, subEnd));
            subStart = subEnd;
            subEnd += 4000;
        }
        LL.i(message.substring(subStart));
    }

    private void logLine(String format, Object... args) {
        logLine(String.format(format, args));
    }

    @Override
    public String toString() {
        return "HttpInfoEntity{" +
                "protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", tookMills=" + tookMills +
                ", requestHeaders=" + requestHeaders +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestContentLength=" + requestContentLength +
                ", requestBody='" + requestBody + '\'' +
                ", responseHeaders=" + responseHeaders +
                ", responseCode=" + responseCode +
                ", responseMessage='" + responseMessage + '\'' +
                ", responseContentLength=" + responseContentLength +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
