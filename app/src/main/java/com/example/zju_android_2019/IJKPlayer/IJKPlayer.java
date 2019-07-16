package com.example.zju_android_2019.IJKPlayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class IJKPlayer extends FrameLayout {

	private SurfaceView mSurfaceView;
	private IMediaPlayer mMediaPlayer;
	private IJKPlayerListener mListener;

	private String mPath;
	private int mResourceId;

	public IJKPlayer(@NonNull Context context) {
		super(context);
		initView();
	}

	public IJKPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public IJKPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public IJKPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
//		setFocusable(true);
	}

	public void setPlayerListener(IJKPlayerListener listener) {
		mListener = listener;
		applyListenerOnPlayer();
	}

	private void applyListenerOnPlayer() {
		if (mMediaPlayer != null && mListener != null) {
			mMediaPlayer.setOnPreparedListener(mListener);
			mMediaPlayer.setOnInfoListener(mListener);
			mMediaPlayer.setOnSeekCompleteListener(mListener);
			mMediaPlayer.setOnBufferingUpdateListener(mListener);
			mMediaPlayer.setOnErrorListener(mListener);
		}
	}

	public void setVideoPath(String path) {
		mPath = path;
		loadVideoByPath();
		createSurfaceView();
	}

	public void setVideoResource(int resId) {
		mResourceId = resId;
		loadVideoByResourceId();
		createSurfaceView();
	}

	private void loadVideoByPath() {
		createPlayer();
		try {
			mMediaPlayer.setDataSource(mPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadVideoByResourceId() {
		createPlayer();
		AssetFileDescriptor fileDescriptor = getContext().getResources().openRawResourceFd(mResourceId);
		RawDataSourceProvider provider = new RawDataSourceProvider(fileDescriptor);
		mMediaPlayer.setDataSource(provider);
//		mMediaPlayer.setLooping(true);
	}

	private boolean hasSurfaceView() {
		return mSurfaceView != null;
	}

	private void createSurfaceView() {
		if (!hasSurfaceView()) {
			mSurfaceView = new SurfaceView(getContext());
			mSurfaceView.getHolder().addCallback(new SurfaceCallback());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
					, ViewGroup.LayoutParams.MATCH_PARENT);
			mSurfaceView.setLayoutParams(layoutParams);
			this.addView(mSurfaceView);
		}
	}

	private class SurfaceCallback implements SurfaceHolder.Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.prepareAsync();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
		}
	}

	private boolean hasPlayer() {
		return mMediaPlayer != null;
	}

	private void dropPlayer() {
		if (hasPlayer()) {
			mMediaPlayer.stop();
			mMediaPlayer.setDisplay(null);
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void createPlayer() {
		dropPlayer();
		IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
		ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
		ijkMediaPlayer.setSpeed(1f);
		mMediaPlayer = ijkMediaPlayer;
		applyListenerOnPlayer();
		if (hasSurfaceView()) {
			mMediaPlayer.setDisplay(mSurfaceView.getHolder());
			mMediaPlayer.prepareAsync();
		}
	}


	public void start() {
		if (hasPlayer()) {
			mMediaPlayer.start();
		}
	}

	public void release() {
		if (hasPlayer()) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void pause() {
		if (hasPlayer()) {
			mMediaPlayer.pause();
		}
	}

	public void stop() {
		if (hasPlayer()) {
			mMediaPlayer.stop();
		}
	}


	public void reset() {
		if (hasPlayer()) {
			mMediaPlayer.reset();
		}
	}


	public long getDuration() {
		if (hasPlayer()) {
			return mMediaPlayer.getDuration();
		} else {
			return 0;
		}
	}


	public long getCurrentPosition() {
		if (hasPlayer()) {
			return mMediaPlayer.getCurrentPosition();
		} else {
			return 0;
		}
	}

	public boolean isPlaying() {
		if (hasPlayer()) {
			return mMediaPlayer.isPlaying();
		}
		return false;
	}

	public void seekTo(long l) {
		if (hasPlayer()) {
			mMediaPlayer.seekTo(l);
		}
	}


}
