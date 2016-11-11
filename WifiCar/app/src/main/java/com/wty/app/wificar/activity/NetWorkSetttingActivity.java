package com.wty.app.wificar.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wty.app.wificar.R;
import com.wty.app.wificar.util.PreferenceUtil;

/**
 * @Desc 设置IP地址以及端口号
 * @author wty
 **/
public class NetWorkSetttingActivity extends AppCompatActivity {

    EditText et_ip,et_port;
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("自定义WIFI小车IP和端口");

        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        bt_submit = (Button) findViewById(R.id.btn_submit);

        et_ip.setText(PreferenceUtil.getInstance().getIP());
        et_port.setText(PreferenceUtil.getInstance().getPort()+"");

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = saveCode();
                if(!TextUtils.isEmpty(result)){
                    Toast.makeText(NetWorkSetttingActivity.this, result, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NetWorkSetttingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * @Decription 保存编码
     **/
    private void saveToPreference(String ip, String port){
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.IP,ip);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.PORT,Integer.valueOf(port));
    }

    /**
     * @Decription 判断一下编码是否符合规则
     **/
    private String saveCode(){
        //判断是否存在空值
        String ip = et_ip.getText().toString();
        String port = et_port.getText().toString();

        if(TextUtils.isEmpty(ip)){
            return "IP不能为空";
        }

        if(TextUtils.isEmpty(port)){
            return "端口不能为空";
        }

        saveToPreference(ip,port);
        return "";
    }

}
