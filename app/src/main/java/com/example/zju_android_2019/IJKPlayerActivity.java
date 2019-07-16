package com.example.zju_android_2019;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zju_android_2019.IJKPlayer.IJKPlayer;
import com.example.zju_android_2019.IJKPlayer.IJKPlayerListener;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.jaygoo.widget.VerticalRangeSeekBar;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static android.media.AudioManager.FLAG_PLAY_SOUND;

public class IJKPlayerActivity extends AppCompatActivity {

	private static final String TAG = "IJKPlayerActivity";

	private RelativeLayout mPlayerLayout, mLoadingLayout;

	private IJKPlayer mIJKPlayer;
	private int mVideoWidth = 0;
	private int mVideoHeight = 0;

	private RelativeLayout mPlayerBarLayout;
	private boolean mPlayerBarVisibility = true;
	private Button mSettingBtn, mPlayBtn, mStopBtn, mVolumeBtn;
	private SeekBar mSeekBar;
	private boolean mSeekBarIsMoving;
	private VerticalRangeSeekBar mVolumeSeekBar;
	private boolean mVolumeSeekBarIsMoving;

	private TextView mTimeText, mLoadMsgText;
	private ProgressBar mLoadProgressBar;

	private boolean mIsPortrait = true;

	private Handler mHandler;
	public static final int MSG_REFRESH = 1001;


	public static void launch(Activity activity) {
		activity.startActivity(new Intent(activity, IJKPlayerActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		initUI();
		initIJKPlayer();
	}

	@SuppressLint("HandlerLeak")
	private void initUI() {
		mPlayerLayout = findViewById(R.id.video_rl_player);
		mLoadingLayout = findViewById(R.id.video_rl_loading);

		mPlayerBarLayout = findViewById(R.id.video_player_bar);
		mSettingBtn = findViewById(R.id.video_bar_btn_setting);
		mPlayBtn = findViewById(R.id.video_bar_btn_play);
		mStopBtn = findViewById(R.id.video_bar_btn_stop);
		mSeekBar = findViewById(R.id.video_bar_seekBar);
		mTimeText = findViewById(R.id.video_bar_tv_time);
		mVolumeBtn = findViewById(R.id.video_bar_btn_volume);
		mVolumeSeekBar = findViewById(R.id.video_bar_sb_volume);

		mLoadMsgText = findViewById(R.id.video_tv_load_msg);
		mLoadProgressBar = findViewById(R.id.video_pb_loading);

		mIJKPlayer = findViewById(R.id.video_ijkPlayer);

		mStopBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mIJKPlayer.seekTo(0);
				mIJKPlayer.pause();
				refresh();
                /*ijkPlayer.mMediaPlayer.prepareAsync();
                ijkPlayer.mMediaPlayer.seekTo(0);*/
                mStopBtn.setClickable(false);
				mPlayBtn.setText(getResources().getString(R.string.media_play));
			}
		});

		mPlayBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPlayBtn.getText().toString().equals(getResources().getString(R.string.pause))) {
					mIJKPlayer.pause();
					mPlayBtn.setText(getResources().getString(R.string.media_play));
				} else {
					mIJKPlayer.start();
//					if (!mStopBtn.isClickable()) {
						mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
//					}
					mPlayBtn.setText(getResources().getString(R.string.pause));
				}
				if (!mStopBtn.isClickable()) {
					mStopBtn.setClickable(true);
				}
			}
		});

		mSettingBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle();
			}
		});

		mIJKPlayer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mPlayerBarVisibility) {
					mPlayerBarLayout.setVisibility(View.VISIBLE);
					Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_bottom);
					mPlayerBarLayout.startAnimation(animation);
					mPlayerBarVisibility = true;
				} else {
					mPlayerBarLayout.setVisibility(View.INVISIBLE);
					Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bottom);
					mPlayerBarLayout.startAnimation(animation);
					mPlayerBarVisibility = false;
				}
			}
		});

		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (mSeekBarIsMoving) {
					mIJKPlayer.seekTo(mIJKPlayer.getDuration() * seekBar.getProgress() / seekBar.getMax());
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mIJKPlayer.pause();
				mHandler.removeCallbacksAndMessages(null);
				mSeekBarIsMoving = true;
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mSeekBarIsMoving = false;
				mIJKPlayer.seekTo(mIJKPlayer.getDuration() * seekBar.getProgress() / seekBar.getMax());
				mIJKPlayer.start();
				mPlayBtn.setText(getResources().getString(R.string.pause));
				mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
			}
		});

		mVolumeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mVolumeSeekBar.getVisibility() == View.GONE) {
					mVolumeSeekBar.setVisibility(View.VISIBLE);
				} else {
					mVolumeSeekBar.setVisibility(View.GONE);
				}
			}
		});

		mVolumeSeekBar.setRange(0f, 100f);
		mVolumeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
			@Override
			public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
				if (mVolumeSeekBarIsMoving) {
					AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume * (leftValue - rightValue) /  view.getMaxProgress()), FLAG_PLAY_SOUND);
				}
			}

			@Override
			public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
				mVolumeSeekBarIsMoving = true;
			}

			@Override
			public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
				mVolumeSeekBarIsMoving = false;
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case MSG_REFRESH:
						if (mIJKPlayer.isPlaying()) {
							refresh();
							mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
						}
						break;
				}
			}
		};
	}

	private void initIJKPlayer() {
		//加载native库
		try {
			IjkMediaPlayer.loadLibrariesOnce(null);
			IjkMediaPlayer.native_profileBegin("libijkplayer.so");
		} catch (Exception e) {
			this.finish();
		}

		mIJKPlayer = findViewById(R.id.video_ijkPlayer);
		//mIJKPlayer.setVideoResource(R.raw.yuminhong);
		mIJKPlayer.setVideoResource(R.raw.big_buck_bunny);

        /*mIJKPlayer.setVideoResource(R.raw.big_buck_bunny);
        mIJKPlayer.setVideoPath("https://media.w3.org/2010/05/sintel/trailer.mp4");
        mIJKPlayer.setVideoPath("http://vjs.zencdn.net/v/oceans.mp4");*/

		mIJKPlayer.setPlayerListener(new IJKPlayerListener() {
			@Override
			public void onBufferingUpdate(IMediaPlayer mp, int percent) {
			}

			@Override
			public void onCompletion(IMediaPlayer mp) {
				mSeekBar.setProgress(100);
				mPlayBtn.setText("播放");
				mStopBtn.setClickable(false);
			}

			@Override
			public boolean onError(IMediaPlayer mp, int what, int extra) {
				return false;
			}

			@Override
			public boolean onInfo(IMediaPlayer mp, int what, int extra) {
				return false;
			}

			@Override
			public void onPrepared(IMediaPlayer mp) {
				refresh();
				mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
				mVideoWidth = mp.getVideoWidth();
				mVideoHeight = mp.getVideoHeight();
				videoScreenInit();
//				mp.start();
				mLoadingLayout.setVisibility(View.GONE);
			}

			@Override
			public void onSeekComplete(IMediaPlayer mp) {
			}

			@Override
			public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
				mVideoWidth = mp.getVideoWidth();
				mVideoHeight = mp.getVideoHeight();
			}
		});
	}

	private void refresh() {
		long current = mIJKPlayer.getCurrentPosition() / 1000;
		long duration = mIJKPlayer.getDuration() / 1000;
		long current_second = current % 60;
		long current_minute = current / 60;
		long total_second = duration % 60;
		long total_minute = duration / 60;
		String time = String.format("%02d:%02d/%02d:%02d", current_minute, current_second, total_minute, total_second);
		mTimeText.setText(time);
		if (duration != 0) {
			mSeekBar.setProgress((int) (current * mSeekBar.getMax() / duration));
		}

		if (!mVolumeSeekBarIsMoving) {
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			mVolumeSeekBar.setProgress((float) currentVolume / maxVolume * mVolumeSeekBar.getMaxProgress());
		}

	}

	private void videoScreenInit() {
		if (mIsPortrait) {
			toPortrait();
		} else {
			toLandscape();
		}
	}

	private void toggle() {
		if (!mIsPortrait) {
			toPortrait();
		} else {
			toLandscape();
		}
	}

	private void toPortrait() {
		mIJKPlayer.pause();
		mIsPortrait = true;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mVolumeBtn.setVisibility(View.GONE);
		mVolumeSeekBar.setVisibility(View.GONE);

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		float height = wm.getDefaultDisplay().getHeight();
		float ratio = width / height;
		if (width < height) {
			ratio = height/width;
		}

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlayerLayout.getLayoutParams();
		layoutParams.height = (int) (mVideoHeight * ratio);
		layoutParams.width = (int) width;
		mPlayerLayout.setLayoutParams(layoutParams);
		mSettingBtn.setText(getResources().getString(R.string.fullScreek));
		mIJKPlayer.start();
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
	}

	private void toLandscape() {
		mIJKPlayer.pause();
		mIsPortrait = false;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		mVolumeBtn.setVisibility(View.VISIBLE);

		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		float width = wm.getDefaultDisplay().getWidth();
		float height = wm.getDefaultDisplay().getHeight();
		float ratio = width / height;

		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mPlayerLayout.getLayoutParams();

		layoutParams.height = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
		layoutParams.width = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
		mPlayerLayout.setLayoutParams(layoutParams);
		mSettingBtn.setText(getResources().getString(R.string.smallScreen));
		mIJKPlayer.start();
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessageDelayed(MSG_REFRESH, 1000);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mIJKPlayer != null && mIJKPlayer.isPlaying()) {
			mIJKPlayer.stop();
		}
		IjkMediaPlayer.native_profileEnd();
		mHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onDestroy() {
		if (mIJKPlayer != null) {
			mIJKPlayer.stop();
			mIJKPlayer.release();
			mIJKPlayer = null;
		}

		super.onDestroy();
	}

}


