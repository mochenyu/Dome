package com.example.lenovo.first;

import android.annotation.TargetApi;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        out = findViewById(R.id.textout);
        EditText inp = findViewById(R.id.inp);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        EditText inp = findViewById(R.id.inp);
        if (inp.length() == 0) {
            Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
        }
        else {
            String str = inp.getText().toString();
            double ds = Double.valueOf(str);
            double dh = ds * 1.8 + 32;
            TextView re = findViewById(R.id.textout);
            String r = re.getText().toString();
            DecimalFormat df = new DecimalFormat("#.00");
            out.setText(r+df.format(dh)+"℉");
        }
        Log.i("main","btn clicked");
    }
}
