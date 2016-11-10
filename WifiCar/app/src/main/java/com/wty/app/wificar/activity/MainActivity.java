package com.wty.app.wificar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.wty.app.wificar.R;


public class MainActivity extends AppCompatActivity {

    private ImageButton btngo,btnstop,btnleft,btnright,btnback;
    TextView tv_setting;

    private String upCode="1";
    private String backCode="2";
    private String leftCode="3";
    private String rightCode="4";
    private String stopCode="0";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private String mConnectedDeviceName = null;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                        sendMessage(upCode);
                        break;

                    case MotionEvent.ACTION_UP:
                        btngo.setBackgroundResource(R.mipmap.up);
                        sendMessage(stopCode);
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
                        sendMessage(leftCode);
                        break;
                    case MotionEvent.ACTION_UP:
                        btnleft.setBackgroundResource(R.mipmap.left);
                        sendMessage(stopCode);
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
                        sendMessage(rightCode);
                        break;

                    case MotionEvent.ACTION_UP:
                        btnright.setBackgroundResource(R.mipmap.right);
                        sendMessage(stopCode);
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
                        sendMessage(backCode);
                        break;

                    case MotionEvent.ACTION_UP:
                        btnback.setBackgroundResource(R.mipmap.back);
                        sendMessage(stopCode);
                        break;
                }
                return false;
            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(stopCode);
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

    }
}
