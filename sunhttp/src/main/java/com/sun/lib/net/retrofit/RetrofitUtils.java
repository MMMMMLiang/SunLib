package com.sun.lib.net.retrofit;


import com.sun.lib.net.SunHttp;
import com.sun.lib.net.observer.HttpObserver;
import com.sun.lib.net.utils.HttpsUtil;
import com.sun.lib.net.utils.LogUtils;
import com.sun.lib.net.utils.SunRequestUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工具类
 * 获取Retrofit 默认使用OkHttpClient
 */
public class RetrofitUtils {
    private String TAG = "HTTP_ApiRetrofit %s";

    private static RetrofitUtils instance = null;
    private static Retrofit.Builder retrofit;

    private RetrofitUtils() {
        retrofit = new Retrofit.Builder();
    }

    public static RetrofitUtils get() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Retrofit
     *
     * @param headerMap 请求头
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, Map<String, Object> headerMap, HttpObserver httpObserver) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit
                .client(getOkHttpClientBase(headerMap, httpObserver))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl
     * @param client
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, OkHttpClient client) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }

    /**
     * 获取OkHttpClient
     * 备注:下载时不能使用OkHttpClient单例,在拦截器中处理进度会导致多任务下载混乱
     *
     * @param newClient        是否新建 OkHttpClient
     * @param timeout 超时时间
     * @param interceptorArray 拦截器
     * @return
     */
    public OkHttpClient getOkHttpClient(boolean newClient, long timeout, TimeUnit timeUnit, Interceptor... interceptorArray) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        /*if (newClient) {
            okHttpClient = new OkHttpClient.Builder();
        }*/
        //超时设置
        okHttpClient.connectTimeout(timeout, timeUnit)
                .writeTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit);

        /**
         * https设置
         * 备注:信任所有证书,不安全有风险
         */
        HttpsUtil.SSLParams sslParams = HttpsUtil.getSslSocketFactory();
        okHttpClient.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        /**
         * 配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
         * 备注:google平台不允许直接返回true
         */
        //okHttpClient.hostnameVerifier(new HostnameVerifier() {        });

        //Interceptor设置
        if (interceptorArray != null) {
            for (Interceptor interceptor : interceptorArray) {
                okHttpClient.addInterceptor(interceptor);
            }
        }
        return okHttpClient.build();
    }

    /**
     * 获取下载时使用 OkHttpClient
     *
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClientDownload(Interceptor... interceptorArray) {
        final long timeout = 60;//超时时长
        final TimeUnit timeUnit = TimeUnit.SECONDS;//单位秒
        return getOkHttpClient(true, timeout, timeUnit, interceptorArray);
    }

    /**
     * 获取基础Http请求使用 OkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClientBase(final Map<String, Object> headerMap, final HttpObserver httpObserver) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("日志拦截器:" + message);
            }
        });
        //must
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //Header 拦截器
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder();
                //统一设置 Header
                if (headerMap != null && headerMap.size() > 0) {
                    for (String key : headerMap.keySet()) {
                        requestBuilder.addHeader(key, String.valueOf(SunRequestUtil.getHeaderValueEncoded(headerMap.get(key))));
                    }
                }
                return chain.proceed(requestBuilder.build());
            }
        };
        //网络请求拦截器
        Interceptor httpInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                long startTime = System.currentTimeMillis();
                Request request = chain.request();
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (final Exception e) {
                    //httpObserver.onCanceled();
                    throw e;
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                if (response.body() != null){
                    MediaType mediaType = response.body().contentType();
                    String content = response.body().string();

                    LogUtils.wtf(TAG, "----------Request Start----------------");
                    LogUtils.e(TAG, "| " + request.toString() + "===========" + request.headers().toString());
                    LogUtils.e(content);
                    LogUtils.json(content);
                    LogUtils.wtf(TAG, "----------Request End:" + duration + "毫秒----------");

                    return response.newBuilder()
                            .body(ResponseBody.create(mediaType, content))
                            .build();
                }else {
                    return null;
                }
            }
        };

        Interceptor[] interceptorArray = new Interceptor[]{logInterceptor, headerInterceptor, httpInterceptor};
        return getOkHttpClient(false, SunHttp.Configure.get().getTimeout(), SunHttp.Configure.get().getTimeUnit(), interceptorArray);
    }
}
