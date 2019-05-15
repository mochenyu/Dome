package com.example.lenovo.first;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable{
    public  final String TAG = "RateActivity" ;
    EditText rmb;
    TextView show;
    private float dollarRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;
    Handler handler;
    private String updateDate = "";

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showout);
        //获取sp里保存的数据
        SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");
        //获得当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);
        Log.i(TAG, "onCreate: sp dollarRate="+dollarRate);
        Log.i(TAG, "onCreate: sp euroRate="+euroRate);
        Log.i(TAG, "onCreate: sp wonRate="+wonRate);
        Log.i(TAG, "onCreate: sp todayStr="+todayStr);
        //判断时间
        if (!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else {
            Log.i(TAG, "onCreate: 不需要更新");
        }
        
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");
                    Log.i(TAG, "handleMessage: dollar:"+dollarRate);
                    Log.i(TAG, "handleMessage: euro:"+euroRate);
                    Log.i(TAG, "handleMessage: won:"+wonRate);
                    //保存更新日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();
                    Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View btn){
        String str = rmb.getText().toString();
        if (str.length() == 0) {
            Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
        }
        else {
            float r = Float.parseFloat(str);
            float val = 0;
            if (btn.getId() == R.id.btn_dollar){
                val = r * dollarRate;
            }else if (btn.getId() == R.id.btn_euro){
                val = r * euroRate;
            }else {
                val = r * wonRate;
            }
            DecimalFormat df;
            df = new DecimalFormat("#.00");
            show.setText(String.valueOf(df.format(val)));
        }
    }

    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        Log.i("open","openOne:");
        Intent config = new Intent(this,CfgActivity.class);
        //Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        //Intent tel = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"));
        config.putExtra("dollar_rate_new","dollarRate");
        config.putExtra("euro_rate_new","euroRate");
        config.putExtra("won_rate_new","wonRate");
        Log.i("", "openOne: dollar_rate_new="+dollarRate);
        Log.i("", "openOne: euro_rate_new="+euroRate);
        Log.i("", "openOne: won_rate_new="+wonRate);
        startActivityForResult(config,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("new_dollarRate",1/6.7f);
            euroRate = bundle.getFloat("new_euroRate",1/11.0f);
            wonRate = bundle.getFloat("new_wonRate",500);
            Log.i(TAG, "onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG, "onActivityResult: euroRate="+euroRate);
            Log.i(TAG, "onActivityResult: wonRate="+wonRate);
            //将新设置的汇率写到sp里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG, "onActivityResult: 数据已保存");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_set){
            openConfig();
        }else if (item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this,List_itemActivity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    public void run() {
        Log.i(TAG, "run: run().......");
        for (int i=1;i<3;i++){
            Log.i(TAG, "run: i="+i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //用于保存获取的汇率
        Bundle bundle;
        //获取网络数据
        /*URL url = null;
        try {
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStreamToString(in);
            Log.i(TAG, "run: html="+html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        bundle = getFromBOC();
        //bundle中保存所获取的汇率
        //获取msg对象，返回主线程
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "Hello from run()";
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    //从bankofchina获取数据
    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
            /*for (Element table : tables){
                Log.i(TAG, "run: table["+i+"]="+table);
                i++;
            }*/
            Element table2 = tables.get(1);
            Log.i(TAG, "run: table6="+table2);
            //获取td中的数据
            Elements tds = table2.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=8){
                Element td1 =tds.get(i);
                Element td2 =tds.get(i+5);
                Log.i(TAG, "run: text="+td1.text()+"==>"+td2.text());
                String str1 = td1.text();
                String val = td2.text();
                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

/*    private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables = doc.getElementsByTag("table");
            *//*for (Element table : tables){
                Log.i(TAG, "run: table["+i+"]="+table);
                i++;
            }*//*
            Element table6 = tables.get(5);
            Log.i(TAG, "run: table6="+table6);
            //获取td中的数据
            Elements tds = table6.getElementsByTag("td");
            for (int i=0;i<tds.size();i+=8){
                Element td1 =tds.get(i);
                Element td2 =tds.get(i+5);
                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run: text="+str1+"==>"+val);

                if ("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }*/

    private String inputStreamToString(InputStream inputStream) throws IOException {
        //将输入流转为字符串
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"gb2312");
        for (; ; ){
            int rsz = in .read(buffer,0,buffer.length);
            if (rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }
}
