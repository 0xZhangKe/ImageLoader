package com.zhangke.imageloader.sample;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhangke.common.base.BaseAppCompatActivity;
import com.zhangke.imageloader.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.img)
    ImageView img;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
//        Glide.with(this)
//                .load("http://himg2.huanqiu.com/attachment2010/2017/0425/20170425103700631.jpg")
//                .into(img);
        ImageLoader.load(img, "http://himg2.huanqiu.com/attachment2010/2017/0425/20170425103700631.jpg");
    }
}
