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

import java.util.LinkedHashMap;
import java.util.Map;

public class CodeSetttingActivity extends AppCompatActivity {

    EditText et_stop,et_up,et_down,et_left,et_right;
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("自定义编码");

        et_stop = (EditText) findViewById(R.id.et_stop);
        et_up = (EditText) findViewById(R.id.et_go);
        et_left = (EditText) findViewById(R.id.et_left);
        et_right = (EditText) findViewById(R.id.et_right);
        et_down = (EditText) findViewById(R.id.et_back);
        bt_submit = (Button) findViewById(R.id.btn_submit);

        et_down.setText(PreferenceUtil.getInstance().getDownCode());
        et_up.setText(PreferenceUtil.getInstance().getUpCode());
        et_left.setText(PreferenceUtil.getInstance().getLeftCode());
        et_right.setText(PreferenceUtil.getInstance().getRightCode());
        et_stop.setText(PreferenceUtil.getInstance().getStopCode());

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = saveCode();
                if(!TextUtils.isEmpty(result)){
                    Toast.makeText(CodeSetttingActivity.this, result, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CodeSetttingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * @Decription 保存编码
     **/
    private void saveToPreference(String down, String up, String left, String right, String stop){
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.DOWN_CODE,down);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.UP_CODE,up);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.LEFT_CODE,left);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.RIGHT_CODE,right);
        PreferenceUtil.getInstance().writePreferences(PreferenceUtil.STOP_CODE,stop);
    }

    /**
     * @Decription 判断一下编码是否符合规则
     **/
    private String saveCode(){
        //判断是否存在空值
        String down = et_down.getText().toString();
        String up = et_up.getText().toString();
        String left = et_left.getText().toString();
        String right = et_right.getText().toString();
        String stop = et_stop.getText().toString();

        if(TextUtils.isEmpty(down) || TextUtils.isEmpty(up) || TextUtils.isEmpty(left) || TextUtils.isEmpty(right) || TextUtils.isEmpty(stop)){
            return "编码不能为空";
        }

        Map<String,String> map = new LinkedHashMap<>();
        map.put(down,down);
        map.put(up,up);
        map.put(left,left);
        map.put(right,right);
        map.put(stop,stop);

        if(map.size()<5){
            return "编码不能相同";
        }

        saveToPreference(down,up,left,right,stop);

        return "";
    }

}
