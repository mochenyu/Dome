package com.example.lenovo.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class JfqActivity extends AppCompatActivity{

    public  final String TAG = "JfqActivity" ;
    TextView score;
    TextView score2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jfq);
        score = findViewById(R.id.score);
        score2 = findViewById(R.id.score2);
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = score.getText().toString();
        String scoreb = score2.getText().toString();
        Log.i(TAG, "onSaveInstanceState: ");
        outState.putString("teamA",scorea);
        outState.putString("teamB",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorcea = savedInstanceState.getString("teamA");
        String scorceb = savedInstanceState.getString("teamB");
        Log.i(TAG, "onRestoreInstanceState: ");
        score.setText(scorcea);
        score2.setText(scorceb);
    }

    public void bt1(View btn) {
        if(btn.getId()==R.id.button1){
            showScore(3);
        }
        else{
            showScore2(3);
        }
    }
    public void bt2(View btn) {
        if(btn.getId()==R.id.button2){
            showScore(2);
        }
        else{
            showScore2(2);
        }
    }
    public void bt3(View btn) {
        if(btn.getId()==R.id.button3){
            showScore(1);
        }
        else{
            showScore2(1);
        }
    }
    public void rs(View btn) {
        score.setText("0");
        score2.setText("0");
    }
    private void showScore(int i){
        Log.i("show","i="+i);
        String os = (String) score.getText();
        int ns = Integer.parseInt(os);
        score.setText(""+ns);
    }
    private void showScore2(int i){
        Log.i("show","i="+i);
        String os = (String) score2.getText();
        int ns = Integer.parseInt(os);
        score2.setText(""+ns);
    }
}
