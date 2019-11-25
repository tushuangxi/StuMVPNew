package com.pgg.mvptest.libding.http.manager;

import com.pgg.mvptest.libding.application.PadApplication;
import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import com.pgg.mvptest.libding.http.ApiService;
import com.pgg.mvptest.libding.http.ServiceMapParams;
import com.pgg.mvptest.libding.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import static com.pgg.mvptest.libding.http.ApiService.SERVER_ADDRESS;

/**
 * Created by PDD on 2017/12/22.
 * 对WeatherAPIService的使用
 */

public class ApiManager {

/*
    private static final Retrofit sRetrofit = new Retrofit .Builder()
            .baseUrl(SERVER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
            .build();
*/

    private static final String TAG = "RetrofitManager";
    //连接超时时间 5s
    private static final long CONNECT_TIMEOUT_SECOND = 5;
    //缓存有效期 1天
    private static final long CACHE_STALE_SECOND = 24 * 60 * 60;
    //缓存大小 100M
    private static final long CACHE_SIZE = 1024 * 1024 * 100;

    private static ApiManager mRetrofitManager = null;
    private static OkHttpClient mOkHttpClient;
    //APIService
    private   ApiService apiManager;


    private ApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_ADDRESS)//指定host       http://demo.api.ycy.com/demoapi/
                .client(getOkHttpClient())//指定OKHttpClient
                .addConverterFactory(GsonConverterFactory.create())//指定转换器，不同的网络请求API规范可自定义转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        //创建APIService
        createAPIService(retrofit);

    }

    // 配置OkHttpClient
    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (ApiManager.class) {
                if (mOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(PadApplication.getContext().getCacheDir(), "HttpCache"),
                            CACHE_SIZE);

                    mOkHttpClient = new OkHttpClient.Builder().cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(loggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS).build();
                }
            }
        }
        return mOkHttpClient;
    }

    public static ApiManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (ApiManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new ApiManager();
                }
            }
        }
        return mRetrofitManager;
    }

    /**
     * 根据网络状况获取缓存的策略
     */
    private String getCacheControl() {
        if (NetworkUtils.isConnected(PadApplication.getContext())) {
            //网络畅通情况下，设置max-age=0，表示不读取缓存，直接去服务器请求最新的数据
            return "max-age=0";
        } else {
            //网络不畅通情况下，读取缓存，并设置缓存时间为CACHE_STALE_SECOND（1天）
            return "only-if-cached, max-stale=" + CACHE_STALE_SECOND;
        }
    }

    // server响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected(PadApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
//                LogUtils.e(TAG, "no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetworkUtils.isConnected(PadApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .header("Content-Type", "application/json")
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SECOND)
                        .removeHeader("Pragma").build();
            }

        }
    };

    /**

     * Function：打印请求参数和返回结果
     */

    // 打印json数据拦截器
    private Interceptor loggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();

            long t1 = System.nanoTime();//请求发起的时间
//        com.orhanobut.logger.Logger.e(String.format("发送请求 %s %n%s",
//                request.url(),  chain.request().body().toString()));

            com.orhanobut.logger.Logger.e(String.format("发送请求 %s %n%s", request.url(),  chain.request().body()==null?"NONE":chain.request().body().toString()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            com.orhanobut.logger.Logger.e(String.format(Locale.getDefault(), "接收响应: [%s] %n返回json:【%s】 %.1fms", response.request().url(), responseBody.string(), (t2 - t1) / 1e6d));
            return response;
        }
    };




    //创建APIService
    private void createAPIService(Retrofit retrofit) {

        apiManager = retrofit.create(ApiService.class);
        //TODO: 这里创建更多的APIService
    }



//    private static final ApiService apiManager = sRetrofit.create(ApiService.class);

    /**
     * 获取天气数据
     * @param city
     * @return
     */
    public  Observable<WeatherRsp> getWeatherData(String format, String city) {
        return apiManager.getWherData(format,city,"e9750ded30ad2caf6f3c2dee31504c6e");
    }

    /**
     *
     * @param
     * @return
     */
    public  Observable<GetListRsp> getGetListRspData(Map<String, String> params) {
        return apiManager.requestGetListRspList(params);
    }
}
