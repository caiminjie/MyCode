package com.example.administrator.communication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private MsgService msgService;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定Service com.example.communication.MSG_ACTION
        Intent intent = new Intent(MainActivity.this, MsgService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgService.startDownload();
            }
        });


    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            msgService = ((MsgService.MsgBinder)service).getService();

            //注册回调接口来接收下载进度的变化
            msgService.setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    mProgressBar.setProgress(progress);
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }

}
