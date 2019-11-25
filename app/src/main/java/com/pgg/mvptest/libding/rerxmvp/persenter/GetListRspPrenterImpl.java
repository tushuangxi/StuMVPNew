package com.pgg.mvptest.libding.rerxmvp.persenter;

import android.util.Log;
import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.rerxmvp.base.BasePresenter;
import com.pgg.mvptest.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.pgg.mvptest.libding.rerxmvp.model.GetAllDataListModelImpl;
import java.util.Map;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by PDD on 2017/12/22.
 */

public class GetListRspPrenterImpl implements interfaceUtilsAll.GetListRspPresenter {
    /**
     * WeatherModel和WeatherView都是通过接口来实现，这就Java设计原则中依赖倒置原则使用
     */
    private interfaceUtilsAll.GetListRspView mGetListRspView;

    /**
     * 封装请求过程的rxjava与retrofit
     */
    Subscriber<GetListRsp> subscriber=new Subscriber<GetListRsp>() {
        @Override
        public void onCompleted() {
            mGetListRspView.hideProgress();
        }

        @Override
        public void onError(Throwable e) {
            mGetListRspView.loadError(e);
            mGetListRspView.hideProgress();
            Log.d("tag-------", "onFailure" + e.getMessage());
            Log.e("tag-------",Log.getStackTraceString(e));
        }

        @Override
        public void onNext(GetListRsp getListRsp) {
            mGetListRspView.loadWeather(getListRsp);
            mGetListRspView.hideProgress();
        }
    };

    public GetListRspPrenterImpl(interfaceUtilsAll.GetListRspView mGetListRspView) {
        this.mGetListRspView = mGetListRspView;

    }


    @Override
    public void getListRspData(Map<String, String> params) {
        mGetListRspView.ShowProgress();
       new BasePresenter().addSubscription(new GetAllDataListModelImpl().getGetListRspData(subscriber,params));
    }

}
