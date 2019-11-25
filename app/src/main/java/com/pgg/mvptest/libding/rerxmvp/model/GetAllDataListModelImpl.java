package com.pgg.mvptest.libding.rerxmvp.model;

import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import com.pgg.mvptest.libding.http.manager.ApiManager;
import com.pgg.mvptest.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetAllDataListModelImpl  implements interfaceUtilsAll.SubjectBaseListModel{


    @Override
    public Subscription getWeatherData(Subscriber<WeatherRsp> subscriber, String format, String city) {

        Observable<WeatherRsp> weatherData = ApiManager.getInstance().getWeatherData(format, city);
        Subscription subscribe = weatherData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
        return subscribe;
    }

    @Override
    public Subscription getGetListRspData(Subscriber<GetListRsp> subscriber, Map<String, String> params) {
        Observable<GetListRsp> getListRspData = ApiManager.getInstance().getGetListRspData(params);
        Subscription subscribe = getListRspData.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
        return subscribe;
    }
}
