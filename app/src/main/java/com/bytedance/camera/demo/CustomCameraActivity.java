package com.bytedance.camera.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_IMAGE;
import static com.bytedance.camera.demo.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.bytedance.camera.demo.utils.Utils.getOutputMediaFile;

public class CustomCameraActivity extends AppCompatActivity {
    private static final String TAG = "CustomCameraActivity";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;
    private int mCameraId;
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            Matrix matrix = new Matrix();
            Log.e(TAG, "onPictureTaken: " + mRotationDegree);
            matrix.postRotate(mRotationDegree);
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();
        }
    };
    private boolean isAutoZoom = true;

    private boolean isRecording = false;

    private int mRotationDegree = 0;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_camera);

        mSurfaceView = findViewById(R.id.img);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                prepareCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCameraAndPreview();
            }
        });

        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            mCamera.takePicture(null, null, mPictureCallback);
        });

        findViewById(R.id.btn_record).setOnClickListener(v -> {
            if (isRecording) {
                releaseMediaRecorder();
                mCamera.startPreview();
                isRecording = false;
                ((Button)findViewById(R.id.btn_record)).setText("Record");
                ((Button) findViewById(R.id.btn_pause)).setText("pause");
                ((Button) findViewById(R.id.btn_pause)).setVisibility(View.INVISIBLE);
            } else {
                mCamera.stopPreview();
                Log.e(TAG, "onCreate: stop camera");
                prepareVideoRecorder();
                isRecording = true;
                ((Button)findViewById(R.id.btn_record)).setText("Stop");
                ((Button) findViewById(R.id.btn_pause)).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.btn_facing).setOnClickListener(v -> {
            releaseCameraAndPreview();
            if (CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK) {
                CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else {
                CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
            prepareCamera();
        });

        findViewById(R.id.btn_zoom).setOnClickListener(v -> {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });
        });
        if (isAutoZoom) {
            ((Button)findViewById(R.id.btn_zoom)).setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.btn_delay).setOnClickListener(v -> {
            ((Button)findViewById(R.id.btn_delay)).setClickable(false);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCamera.takePicture(null, null, mPictureCallback);
                    ((Button)findViewById(R.id.btn_delay)).setClickable(true);
                }
            }, 3000);
        });

        findViewById(R.id.btn_flash).setOnClickListener(v -> {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
            mCamera.setParameters(parameters);
        });

        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((Button)findViewById(R.id.btn_pause)).getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (s.equals("pause")) {
                        mMediaRecorder.pause();
                        ((Button) findViewById(R.id.btn_pause)).setText("resume");
                    } else {
                        mMediaRecorder.resume();
                        ((Button) findViewById(R.id.btn_pause)).setText("pause");
                    }
                }
            }
        });

        findViewById(R.id.btn_auto_zoom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAutoZoom) {
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    mCamera.setParameters(parameters);
                    ((Button)findViewById(R.id.btn_zoom)).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.btn_auto_zoom)).setText("manual zoom");
                    isAutoZoom = false;
                } else {
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    mCamera.setParameters(parameters);
                    ((Button)findViewById(R.id.btn_zoom)).setVisibility(View.INVISIBLE);
                    ((Button)findViewById(R.id.btn_auto_zoom)).setText("auto zoom");
                    isAutoZoom = true;
                }
            }
        });

    }

    public Camera getCamera() {
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_TYPE) {
                mCameraId = i;
                break;
            }
        }
        Camera cam = Camera.open(mCameraId);

        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等
        mRotationDegree = getCameraDisplayOrientation(mCameraId);
        cam.setDisplayOrientation(mRotationDegree);
        Camera.Parameters parameters = cam.getParameters();
        if (isAutoZoom) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        cam.setParameters(parameters);

        return cam;
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        return getCamera();
    }


    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
	    mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    Camera.Size size;

    private void prepareCamera() {
        mCamera = getCamera();
        // 开始预览
	    try {
		    mCamera.setPreviewDisplay(mSurfaceHolder);
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
	    mCamera.startPreview();
    }

    private void prepareCamera(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        prepareCamera();
    }


    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        // 准备MediaRecorder

        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setOrientationHint(mRotationDegree);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }

        mMediaRecorder.start();

        return true;
    }


    private void releaseMediaRecorder() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
    }


    private Camera.PictureCallback mPicture = (data, camera) -> {
        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }

        mCamera.startPreview();
    };


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }
}
