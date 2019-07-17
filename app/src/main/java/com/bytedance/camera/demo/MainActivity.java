package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bytedance.camera.demo.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 757;
    private String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TakePictureActivity.class));
        });

        findViewById(R.id.btn_camera).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
        });

        findViewById(R.id.btn_custom).setOnClickListener(v -> {
            // 在这里申请相机、麦克风、存储的权限
            Utils.reuqestPermissions(this, permissions, REQUEST_PERMISSIONS);
            startActivity(new Intent(MainActivity.this, CustomCameraActivity.class));

        });
    }

}
