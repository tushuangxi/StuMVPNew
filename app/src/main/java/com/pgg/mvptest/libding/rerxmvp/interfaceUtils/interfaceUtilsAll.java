package com.pgg.mvptest.libding.rerxmvp.interfaceUtils;

import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import java.util.Map;
import rx.Subscriber;
import rx.Subscription;

public class interfaceUtilsAll {


    public interface SubjectBaseListModel {
        //此接口定义获取天气信息的方法，返回一个Subscription，便于注销监听  WeatherRsp
        Subscription getWeatherData(Subscriber<WeatherRsp> subscriber, String format, String city);
        //GetListRsp
        Subscription getGetListRspData(Subscriber<GetListRsp> subscriber, Map<String, String> params);
    }


//----------------------------------------------------------
    public interface WeatherPreenter {
        void getWeatherData(String format, String city);
    }

    //WeatherRsp
    public interface WeatherView {
        //此接口主要用于定义显示view的一些方法
        void ShowProgress();//显示加载进度条
        void hideProgress();//隐藏加载进度条
        void loadWeather(WeatherRsp weatherRsp);//通过传递过来的天气数据，将数据展示在控件上
    }

    //----------------------------------------------------------
    //GetListRsp
    public interface GetListRspView {
        //此接口主要用于定义显示view的一些方法
        void ShowProgress();//显示加载进度条
        void hideProgress();//隐藏加载进度条
        void loadWeather(GetListRsp getListRsp);

        void loadError(Throwable e);
    }

    public interface GetListRspPresenter {
        void getListRspData(Map<String, String> params);
    }



}
