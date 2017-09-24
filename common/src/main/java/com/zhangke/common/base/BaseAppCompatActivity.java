package com.zhangke.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhangke.common.uils.UiUtils;
import com.zhangke.common.widget.RoundProgressDialog;

/**
 * Created by ZhangKe on 2017/9/24.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseActivity {

    protected final String TAG = this.getClass().getSimpleName();

    private RoundProgressDialog roundProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        roundProgressDialog = new RoundProgressDialog(this);
        initView();
        initView(savedInstanceState);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void showToastMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UiUtils.showToast(BaseAppCompatActivity.this, msg);
            }
        });
    }

    /**
     * 显示圆形加载对话框，默认消息（请稍等...）
     */
    @Override
    public void showRoundProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundProgressDialog.showProgressDialog();
            }
        });
    }

    /**
     * 显示圆形加载对话框
     *
     * @param msg 提示消息
     */
    @Override
    public void showRoundProgressDialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundProgressDialog.showProgressDialog(msg);
            }
        });
    }

    /**
     * 关闭圆形加载对话框
     */
    @Override
    public void closeRoundProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundProgressDialog.closeProgressDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
