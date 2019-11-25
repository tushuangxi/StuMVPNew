package com.pgg.mvptest.libding.rerxmvp.persenter;

import android.util.Log;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import com.pgg.mvptest.libding.rerxmvp.base.BasePresenter;
import com.pgg.mvptest.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.pgg.mvptest.libding.rerxmvp.model.GetAllDataListModelImpl;

import java.util.Map;

import rx.Subscriber;

/**
 * Created by PDD on 2017/12/22.
 */

public class WeatherRspPrenterImpl implements interfaceUtilsAll.WeatherPreenter {
    /**
     * WeatherModel和WeatherView都是通过接口来实现，这就Java设计原则中依赖倒置原则使用
     */
    private interfaceUtilsAll.SubjectBaseListModel mWeatherModel;
    private interfaceUtilsAll.WeatherView mWeatherView;

    /**
     * 封装请求过程的rxjava与retrofit
     */
    Subscriber<WeatherRsp> subscriber=new Subscriber<WeatherRsp>() {
        @Override
        public void onCompleted() {
            mWeatherView.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            mWeatherView.hideProgress();
            Log.d("-------", "onFailure" + e.getMessage());
        }

        @Override
        public void onNext(WeatherRsp weatherRsp) {
            mWeatherView.loadWeather(weatherRsp);
            mWeatherView.hideProgress();
        }
    };

    public WeatherRspPrenterImpl(interfaceUtilsAll.WeatherView mWeatherView) {
        this.mWeatherView = mWeatherView;
    }


    @Override
    public void getWeatherData(String format, String city) {
        mWeatherView.ShowProgress();
        new BasePresenter().addSubscription(new GetAllDataListModelImpl().getWeatherData(subscriber,format,city));
    }

}
