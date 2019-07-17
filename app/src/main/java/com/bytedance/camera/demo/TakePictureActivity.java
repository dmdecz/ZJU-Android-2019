package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TakePictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private File imageFile;
    String[] permissions = new String[] {
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        imageView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (Utils.isPermissionsReady(TakePictureActivity.this,permissions)) {
                takePicture();
            } else {
                // 在这里申请相机、存储的权限
                Utils.reuqestPermissions(TakePictureActivity.this,permissions,REQUEST_EXTERNAL_STORAGE);
            }
        });

    }

    private void takePicture() {
        // 打开相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
        if (imageFile != null) {
            Uri fileUri = FileProvider.getUriForFile(this, "com.bytedance.camera.demo", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        data.getData();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 处理返回数据
            setPic();
        }
    }

    private void setPic() {
        // 根据imageView裁剪
        // 根据缩放比例读取文件，生成Bitmap
        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        BitmapFactory.Options bmtOptions = new BitmapFactory.Options();
        bmtOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmtOptions);

        int photoWidth = bmtOptions.outWidth;
        int photoHeight = bmtOptions.outHeight;

        int scaleFactor = Math.min(photoWidth / targetWidth, photoHeight / targetHeight);

        bmtOptions.inJustDecodeBounds = false;
        bmtOptions.inSampleSize = scaleFactor;
        bmtOptions.inPurgeable = true;

        //todo 如果存在预览方向改变，进行图片旋转
        Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmtOptions);
        imageView.setImageBitmap(bmp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // 判断权限是否已经授予
                if (Utils.isPermissionsReady(TakePictureActivity.this,permissions)){
                    takePicture();
                }
                break;
            }
        }
    }

}
