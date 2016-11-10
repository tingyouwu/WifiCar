package com.wty.app.wificar.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wty.app.wificar.R;
import com.wty.app.wificar.base.Constant;
import com.wty.app.wificar.event.RefreshEvent;
import com.wty.app.wificar.wifi.WifiChatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class LoginActivity extends AppCompatActivity {

    Button btn_connect;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                WifiChatService.getInstance().start();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(RefreshEvent event){
        Log.d("wutingyou:",event.getMsg());
        if(event.getMsg().equals(Constant.Connect_Fail)){
            //提示连接失败
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"无法连接上wifi小车，请检查!",Toast.LENGTH_SHORT).show();
                }
            });

        }else if(event.getMsg().equals(Constant.Connect_Success)){
            //连接成功
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,"连接上wifi小车",Toast.LENGTH_SHORT).show();
                    WifiChatService.getInstance().write("1".getBytes());
                }
            });
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("WIFI小车");
        progressDialog.setMessage("正在连接小车，请等待...");
        progressDialog.setCancelable(false);// 设置对话框能否用back键取消
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置对话框风格
        progressDialog.show();
    }

}
