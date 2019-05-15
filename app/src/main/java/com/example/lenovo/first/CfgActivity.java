package com.example.lenovo.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CfgActivity extends AppCompatActivity {

    public  final String TAG = "CfgActivity" ;
    EditText dollarText;
    EditText euroText;
    EditText wonText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfg);
        Intent intent = getIntent();
        float dollar2 = intent.getFloatExtra("dollar_rate_new",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_new",0.0f);
        float won2 = intent.getFloatExtra("won_rate_new",0.0f);
        Log.i(TAG, "onCreate: dollar2="+dollar2);
        Log.i(TAG, "onCreate: euro2="+euro2);
        Log.i(TAG, "onCreate: won2="+won2);

        dollarText = findViewById(R.id.dollar_rate);
        euroText = findViewById(R.id.euro_rate);
        wonText = findViewById(R.id.won_rate);
        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }

    public void save(View btn){
        Log.i(TAG, "save: ");
        //获取新的值
        float newdollar = Float.parseFloat(dollarText.getText().toString());
        float neweuro = Float.parseFloat(euroText.getText().toString());
        float newwon = Float.parseFloat(wonText.getText().toString());
        Log.i(TAG, "save: 获取到新的值");
        Log.i(TAG, "onCreate: newdollar=" + newdollar);
        Log.i(TAG, "onCreate: neweuro=" + neweuro);
        Log.i(TAG, "onCreate: newwon=" + newwon);
        //保存到bundle或放入到Extra
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putDouble("new_dollarRate",newdollar);
        bdl.putDouble("new_euroRate",neweuro);
        bdl.putDouble("new_wonRate",newwon);
        intent.putExtras(bdl);
        setResult(2,intent);
        //返回调用页面
        finish();//结束当前页面回到上一个页面
    }
}
