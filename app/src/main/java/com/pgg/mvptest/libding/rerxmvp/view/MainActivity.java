package com.pgg.mvptest.libding.rerxmvp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.pgg.mvptest.R;
import com.pgg.mvptest.libding.entry.WeatherRsp;
import com.pgg.mvptest.libding.rerxmvp.interfaceUtils.interfaceUtilsAll;
import com.pgg.mvptest.libding.rerxmvp.persenter.WeatherRspPrenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements interfaceUtilsAll.WeatherView {

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

    private WeatherRspPrenterImpl mWeatherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mWeatherPresenter = new WeatherRspPrenterImpl(this);
        mWeatherPresenter.getWeatherData("2", "西安");

    }

    @Override
    public void ShowProgress() {
        Toast.makeText(this,"正在获取数据...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void loadWeather(WeatherRsp weatherRsp) {
        Log.d("weatherRsp", "weatherRsp==" + weatherRsp.toString());
//        tv_city.setText("城市："+weatherRsp.getResult().getToday().getCity());
//        tv_day.setText("日期："+weatherRsp.getResult().getToday().getWeek());
//        tv_today_temp.setText("今日温度："+weatherRsp.getResult().getToday().getTemperature());
//        tv_temp_icon.setText("天气标识：" +weatherRsp.getResult().getToday().getWeather());
//        tv_num_close.setText("穿衣指数：" + weatherRsp.getResult().getToday().getDressing_advice());
//        tv_num_ganzao.setText("干燥指数："+weatherRsp.getResult().getToday().getDressing_index());
//        tv_num_ziwaixian.setText("紫外线强度："+weatherRsp.getResult().getToday().getUv_index());
//        tv_chuanyijianyi.setText("穿衣建议："+weatherRsp.getResult().getToday().getDressing_advice());
//        tv_lveyouzhishu.setText("旅游指数："+weatherRsp.getResult().getToday().getTravel_index());

        Toast.makeText(this,"请求成功",Toast.LENGTH_LONG).show();
    }
}
