package com.pgg.mvptest.libding.http;

import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.entry.WeatherRsp;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by PDD on 2017/12/22.
 */

public interface ApiService {

    boolean isDebugHost = AppConfig.isDebug;
    //正式环境host
    String hostRelease = "http://api.zhuishushenqi.com";//正式接口

    //测试环境host
    String hostDebug = "http://api.zhuishushenqi.com";

    // 服务器域名  http://api.zhuishushenqi.com/cats/lv2/statistics
    String SERVER_ADDRESS = isDebugHost ? hostDebug : hostRelease;


    //http://v.juhe.cn/weather/index?format=2&cityname=%E8%8B%8F%E5%B7%9E&key=JHcee9b557d3e6c747375d4ae68ee1d9dc
    /**
     * 此接口用于从网络获取数据的接口，使用retrofit框架
     */
    @GET("/weather/index") //http://v.juhe.cn
    Observable<WeatherRsp> getWherData(@Query("format") String format, @Query("cityname") String cityname, @Query("key") String key);

    @GET("/cats/lv2/statistics/")
    Observable<GetListRsp> requestGetListRspList(@QueryMap Map<String, String> params);
}
