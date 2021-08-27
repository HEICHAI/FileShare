package com.bj.nanohttpd.http;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import static com.bj.nanohttpd.utils.GlobalData.DEFAULT_SERVER_PORT;

public class HTTPService extends Service {

    private HTTPServer httpServer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httpServer = new HTTPServer(DEFAULT_SERVER_PORT);
        try {
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            httpServer.stop();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand: service has started.", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        httpServer.stop();
        super.onDestroy();
        Toast.makeText(this, "onDestroy: service has destroyed.", Toast.LENGTH_SHORT).show();
    }
}
