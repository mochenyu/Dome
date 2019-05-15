package com.example.lenovo.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalActivity extends AppCompatActivity {

    public  final String TAG = "RateCalActivity" ;
    float rate = 0.0f;
    EditText inp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_cal);
        String title = getIntent().getStringExtra("title");
        rate = getIntent().getFloatExtra("rate",0.0f);
        Log.i(TAG, "onCreate: title="+title);
        Log.i(TAG, "onCreate: rate="+rate);

        ((TextView)findViewById(R.id.titleplus)).setText(title);
        inp2 = findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView show = RateCalActivity.this.findViewById(R.id.show2);
                if (s.length()>0){
                    float val = Float.parseFloat(s.toString());
                    show.setText(val+"RMB==>"+(100/rate*val));
                }else {
                    show.setText("");
                }

            }
        });
    }
}
