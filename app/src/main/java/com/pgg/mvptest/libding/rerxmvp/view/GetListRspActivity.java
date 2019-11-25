package com.pgg.mvptest.libding.rerxmvp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pgg.mvptest.R;
import com.pgg.mvptest.libding.entry.GetListRsp;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import com.pgg.mvptest.libding.http.ServiceMapParams;
import com.pgg.mvptest.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.pgg.mvptest.libding.rerxmvp.model.GetAllDataListModelImpl;
import com.pgg.mvptest.libding.rerxmvp.persenter.GetListRspPrenterImpl;
import com.pgg.mvptest.libding.rerxmvp.persenter.WeatherRspPrenterImpl;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetListRspActivity extends AppCompatActivity implements interfaceUtilsAll.GetListRspView {

    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_today_temp)
    TextView tv_today_temp;
    @BindView(R.id.tv_temp_icon)
    TextView tv_temp_icon;
    @BindView(R.id.tv_num_close)
    TextView tv_num_close;
    @BindView(R.id.tv_num_ganzao)
    TextView tv_num_ganzao;
    @BindView(R.id.tv_num_ziwaixian)
    TextView tv_num_ziwaixian;
    @BindView(R.id.tv_chuanyijianyi)
    TextView tv_chuanyijianyi;
    @BindView(R.id.tv_lveyouzhishu)
    TextView tv_lveyouzhishu;

    GetListRspPrenterImpl mGetListRspPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGetListRspPresenter = new GetListRspPrenterImpl(this);
        mGetListRspPresenter.getListRspData(ServiceMapParams.getGetListRspMapParams());
    }

    @Override
    public void ShowProgress() {
        Toast.makeText(this,"正在获取数据...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadWeather(GetListRsp getListRsp) {

        Toast.makeText(this,"请求成功",Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadError(Throwable e) {
        Toast.makeText(this,"请求失败",Toast.LENGTH_LONG).show();
    }
}
