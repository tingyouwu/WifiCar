package com.wty.app.wificar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wty.app.wificar.R;
import com.wty.app.wificar.base.Constant;
import com.wty.app.wificar.event.RefreshEvent;
import com.wty.app.wificar.util.PreferenceUtil;
import com.wty.app.wificar.wifi.WifiChatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * @Desc 小车控制页面
 **/
public class MainActivity extends AppCompatActivity {

    private ImageButton btngo,btnstop,btnleft,btnright,btnback;
    TextView tv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //control Button
        btngo = (ImageButton) findViewById(R.id.btngo);
        btnleft = (ImageButton) findViewById(R.id.btnleft);
        btnright = (ImageButton) findViewById(R.id.btnright);
        btnstop = (ImageButton) findViewById(R.id.btnstop);
        btnback = (ImageButton) findViewById(R.id.btnback);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        initListener();
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
        if(event.getMsg().equals(Constant.Connect_Lost)){
            //提示连接失败
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"wifi小车已经断开，请检查!",Toast.LENGTH_SHORT).show();
                }
            });
            Intent serverIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(serverIntent);
            finish();
        }else if(event.getMsg().equals(Constant.Connect_Success)){

        }
    }

    /**
     * @Decription 初始化各个按钮效果
     **/
    private void initListener(){
        btngo.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        btngo.setBackgroundResource(R.mipmap.up_press);
                        sendMessage(PreferenceUtil.getInstance().getUpCode());
                        break;

                    case MotionEvent.ACTION_UP:
                        btngo.setBackgroundResource(R.mipmap.up);
                        sendMessage(PreferenceUtil.getInstance().getStopCode());
                        break;
                }
                return false;
            }


        });
        btnleft.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        btnleft.setBackgroundResource(R.mipmap.left_press);
                        sendMessage(PreferenceUtil.getInstance().getLeftCode());
                        break;
                    case MotionEvent.ACTION_UP:
                        btnleft.setBackgroundResource(R.mipmap.left);
                        sendMessage(PreferenceUtil.getInstance().getStopCode());
                        break;
                }
                return false;
            }


        });
        btnright.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        btnright.setBackgroundResource(R.mipmap.right_press);
                        sendMessage(PreferenceUtil.getInstance().getRightCode());
                        break;

                    case MotionEvent.ACTION_UP:
                        btnright.setBackgroundResource(R.mipmap.right);
                        sendMessage(PreferenceUtil.getInstance().getStopCode());
                        break;
                }
                return false;
            }


        });
        btnback.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        btnback.setBackgroundResource(R.mipmap.back_press);
                        sendMessage(PreferenceUtil.getInstance().getDownCode());
                        break;

                    case MotionEvent.ACTION_UP:
                        btnback.setBackgroundResource(R.mipmap.back);
                        sendMessage(PreferenceUtil.getInstance().getStopCode());
                        break;
                }
                return false;
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(PreferenceUtil.getInstance().getStopCode());
            }
        });

        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serverIntent = new Intent(MainActivity.this, CodeSetttingActivity.class);
                startActivity(serverIntent);
            }
        });
    }

    /**
     * Sends a message.
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        if (WifiChatService.getInstance().getState() != WifiChatService.STATE_CONNECTED) {
            Toast.makeText(this, "尚未连接到WIFI小车,请先连接!", Toast.LENGTH_SHORT).show();
            Intent serverIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(serverIntent);
            finish();
            return;
        }

        if(message.length()>0){
            WifiChatService.getInstance().write(message.getBytes());
        }
    }
}
