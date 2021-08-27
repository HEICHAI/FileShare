package com.hc.nanohttpd;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hc.nanohttpd.http.HTTPService;
import com.hc.nanohttpd.utils.FileUtils;
import com.hc.nanohttpd.utils.SharedFile;

import java.util.ArrayList;

import static com.hc.nanohttpd.utils.FileUtils.path2Name;
import static com.hc.nanohttpd.utils.GlobalData.audioList;
import static com.hc.nanohttpd.utils.GlobalData.fileList;
import static com.hc.nanohttpd.utils.GlobalData.imageList;
import static com.hc.nanohttpd.utils.GlobalData.videoList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<SharedFile> currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    @SuppressLint("NonConstantResourceId")
    public void serviceListener(View view) {
        switch (view.getId()) {
            case R.id.start_service:
                startService(new Intent(this, HTTPService.class));
                break;
            case R.id.stop_service:
                stopService(new Intent(this, HTTPService.class));
                break;
        }
    }

    ActivityResultLauncher<String> launcher = registerForActivityResult(new ResultContract(), new ActivityResultCallback<Intent>() {
        @Override
        public void onActivityResult(Intent intent) {
            Uri uri = intent.getData();
            SharedFile sharedFile = new SharedFile();
            sharedFile.setFilepath(FileUtils.getPath(MainActivity.this, uri));//这个地方就是坑了，直接选择文件的路径在7.0的手机和7.1的手机上面都是不对的
            sharedFile.setFilename(path2Name(FileUtils.getPath(MainActivity.this, uri)));
            currentList.add(sharedFile);
            System.out.println(imageList.toString());
        }
    });

    static class ResultContract extends ActivityResultContract<String, Intent> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String type) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //文件类型
            intent.setType(type + "/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            return intent;
        }

        @Override
        public Intent parseResult(int resultCode, @Nullable Intent intent) {
            return intent;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void selectListener(View view) {
        switch (view.getId()) {
            case R.id.image_select:
                currentList = imageList;
                launcher.launch("image");
                break;
            case R.id.audio_select:
                currentList = audioList;
                launcher.launch("audio");
                break;
            case R.id.video_select:
                currentList = videoList;
                launcher.launch("video");
                break;
            case R.id.file_select:
                currentList = fileList;
                launcher.launch("file");
                break;
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "The permission was obtained successfully!");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
        }
    }
}