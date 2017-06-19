package com.example.administrator.trafficstatsdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "trafficstatsdemo";

    private final static int TRAFFIC_1_MIN = 1;

   //private List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = null;
    double oldtraffic = 0;
    boolean firstenter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: ");
        mHandler.sendEmptyMessage(TRAFFIC_1_MIN);

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "handleMessage: ");
            getTrafficInfo();
        }
    };


    private void getTrafficInfo(){

        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();//得到所有运行的程序
        Log.e(TAG, "the runningAppProcessInfos size is :"+runningAppProcessInfos.size());

        for(ActivityManager.RunningAppProcessInfo runprocess : runningAppProcessInfos){
            int processUid = runprocess.uid;//得到程序的uid

            double nowTraffic = TrafficStats.getUidRxBytes(processUid);
            double usedTraffic = nowTraffic - oldtraffic;

            oldtraffic = nowTraffic;

            if(firstenter == true){
                Log.e(TAG, "this is the first time ");
                firstenter = false;
            }else {
                Log.e(TAG, "the process name is : " + runprocess.processName );
                Log.e(TAG, "in 1 min used traffic :" + usedTraffic);
            }

        }

        mHandler.sendEmptyMessageDelayed(TRAFFIC_1_MIN,10000);

    }

}
